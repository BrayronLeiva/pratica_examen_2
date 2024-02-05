package chatClient.presentation.Controller;

import chatClient.logic.ServiceProxy;
import chatClient.presentation.Model.Model;
import chatClient.presentation.View.View;

import chatProtocol.*;

import javax.swing.*;
import java.util.ArrayList;

public class Controller  {
    View view;
    Model model;
    ServiceProxy localService;
    private Window_Listener windowListener;
    private Button_Listener button_listener;
    private int numeroWorker;
    
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
        this.initCBopciones();
        localService.solicitar_numero_worker();

    }

    public void init_window_listener(){
        windowListener = new Window_Listener(this);
        view.addWindowListener(windowListener);
    }

    public void init_button_listener(){

        button_listener = new Button_Listener(this);
        view.getBtnPoner().addActionListener(button_listener);

    }

    public void login(User u){
        try {
            User logged = ServiceProxy.instance().login(u);
            model.setCurrentUser(logged);
            model.commit(Model.USER);
            ServiceProxy.instance().inicializar_servidor();
        }catch (Exception ex){
            view.lanzar_mensaje("Ya hay 3 jugadores conectados\n");
            System.exit(0);
        }
    }


/*
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

 */

    public void ponerFicha(){
        try {
            Message message = new Message();
            //message.setMessage(text);
            Position obj = this.generar_ficha();
            //if (model.getLista_candidatos().existAlready(obj.getId()))
                //throw new Exception("Este cantidado ya existe\n");
            message.setSender(model.getCurrentUser());
            ServiceProxy.instance().enviar_ficha(obj);
            model.commit(Model.CHAT);
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
        view.limpiar_interfaz();
    }

    public void initCBopciones(){
        view.getCmOption().addItem("A");
        view.getCmOption().addItem("B");
        view.getCmOption().addItem("C");
        view.getCmOption().addItem("D");
        view.getCmOption().addItem("E");
        view.getCmOption().addItem("F");
        view.getCmOption().addItem("G");
    }
/*
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
            //write code--------------------------------------------------------------------------
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
    }
*/
    public Position generar_ficha() {
        String op = String.valueOf(view.getCmOption().getSelectedItem());
        if(op.equals("")){
            System.out.println("VACIO\n");
        }
        int column = (int) this.obtenerColumn(op);
        //validar_excepciones();
        return new Position(column, "gamed", numeroWorker);
    }

    public int obtenerColumn(String op){
        int r= -1;
        switch (op){
            case "A": r = 0; break;
            case "B": r = 1; break;
            case "C": r = 2; break;
            case "D": r = 3; break;
            case "E": r = 4; break;
            case "F": r = 5; break;
            case "G": r = 6; break;
            default: break;
        }
        return r;
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
        view.lanzar_mensaje(message);
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
        //
    }

    public View getView() { return view;}

    public Window_Listener getWindowListener() {
        return windowListener;
    }

    public Button_Listener getButton_listener() {
        return button_listener;
    }

    public void setNumeroWorker(int num){
        this.numeroWorker = num;
        System.out.println("Este es ni numero de worker "+ numeroWorker);
        view.lanzar_mensaje("Eres el Player: "+numeroWorker);
        this.initTurno();
        view.getLbPlayer().setText("Player "+numeroWorker);
    }

    public void initTurno(){
        if (numeroWorker==2){
            view.getCmOption().setEnabled(false);
            view.getBtnPoner().setEnabled(false);
        }
        if(numeroWorker==1){
            view.getCmOption().setEnabled(true);
            view.getBtnPoner().setEnabled(true);
        }
    }

    public void changeTurno(int numW){
        if(numeroWorker!=numW){
            view.getCmOption().setEnabled(true);
            view.getBtnPoner().setEnabled(true);
        }else{
            view.getCmOption().setEnabled(false);
            view.getBtnPoner().setEnabled(false);
        }
    }
}
