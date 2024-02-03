package chatClient.presentation.Controller;

import chatClient.logic.ServiceProxy;
import chatClient.presentation.Model.Model;
import chatClient.presentation.View.View;

import chatProtocol.Candidato;
import chatProtocol.Lista_Candidatos;
import chatProtocol.Message;
import chatProtocol.User;

import javax.swing.*;
import java.util.ArrayList;

public class Controller  {
    View view;
    Model model;
    ServiceProxy localService;
    private Window_Listener windowListener;
    private Button_Listener button_listener;
    
    public Controller() throws Exception {


        this.view = new View(this);;
        this.model = new Model();
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);;
        view.setModel(model);
        this.init_window_listener();
        this.init_button_listener();
        User u = new User();
        this.login(u);
    }

    public void init_window_listener(){
        windowListener = new Window_Listener(this);
        view.getWindow().addWindowListener(windowListener);
    }

    public void init_button_listener(){

        button_listener = new Button_Listener(this);
        view.getBtn_agregar().addActionListener(button_listener);
        view.getBtn_votar().addActionListener(button_listener);

    }

    public void login(User u) throws Exception{
        User logged=ServiceProxy.instance().login(u);
        model.setCurrentUser(logged);
        model.commit(Model.USER);
        ServiceProxy.instance().inicializar_servidor();
    }

    public void agregarCandidato(){
        try {
            Message message = new Message();
            //message.setMessage(text);
            Candidato obj = this.generar_candidato();
            if (model.getLista_candidatos().existAlready(obj.getId()))
                throw new Exception("Este cantidado ya existe\n");
            message.setSender(model.getCurrentUser());
            ServiceProxy.instance().agregar_cantidato(obj);
            model.commit(Model.CHAT);
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
        view.limpiar_interfaz();
    }

    public void efectuarVoto(){

        Message message = new Message();
        //message.setMessage(text);
        message.setSender(model.getCurrentUser());
        try {
            if (!is_item_select()) {
                throw new Exception("Seleccione primero una opcion\n");
            }
            String id = String.valueOf(view.getTable_candidatos().getValueAt(view.getTable_candidatos().getSelectedRow(), 0));
            ServiceProxy.instance().efectuar_voto(id);
            model.commit(Model.CHAT);
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
            view.limpiar_interfaz();
        }
    }

    public boolean is_item_select(){
        boolean r = false;
        int c = view.getTable_candidatos().getRowCount();
        for (int i = 0; i < c; i++){
            if(view.getTable_candidatos().isRowSelected(i))
                r = true;
        } return r;
    }

    public void validar_excepciones() {
        try {
            if (view.getTxf_id().getText().equals("") || view.getTxf_nombre().getText().equals("")) {
                throw new Exception("Campos Vacios - Por favor verifique");
            }
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
    }

    public Candidato generar_candidato() {
        String id = view.getTxf_id().getText();
        String nombre = view.getTxf_nombre().getText();
        validar_excepciones();
        return new Candidato(id, nombre, 0);
    }

    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            model.setMessages(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }
        model.setCurrentUser(null);
        model.commit(Model.USER+Model.CHAT);
    }
        
    public void deliver(String message){
        model.getMessages().add(message);
        System.out.println("Se envio este mensaje: " + message);
        model.commit(Model.CHAT);       
    }
    public void agregar_candidato_lista(Candidato obj){
        model.agregar_candidato(obj);
        model.imprimir_lista_candidatos();
        System.out.println("Se agrego este candidato: " + obj.getNombre());
        model.commit(Model.CHAT);
        this.recargar_tabla();
    }

    public void iniciar_candidato_lista(Lista_Candidatos list) {
        model.setLista_candidatos(list);
        model.imprimir_lista_candidatos();
        System.out.println("Se inicializo la lista");
        model.commit(Model.CHAT);
        this.recargar_tabla();
    }

    public void actualizar_candidato_lista(Candidato obj){
        model.uptade_candidato(obj);
        model.imprimir_lista_candidatos();
        System.out.println("Se actualizo este candidato: " + obj.getNombre());
        model.commit(Model.CHAT);
        this.recargar_tabla();
    }

    public void recargar_tabla(){
        try {
            model.recargar_tabla(view.getTable_candidatos());
        }catch (Exception ex){
            System.out.println("Excepcion es : " + ex.getMessage());
        }
    }

    public View getView() { return view;}

    public Window_Listener getWindowListener() {
        return windowListener;
    }

    public Button_Listener getButton_listener() {
        return button_listener;
    }
}
