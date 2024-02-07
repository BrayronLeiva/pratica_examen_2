package chatClient.presentation.Controller;

import chatClient.logic.ServiceProxy;
import chatClient.presentation.Model.Model;
import chatClient.presentation.View.View;

import chatProtocol.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller  {
    View view;
    Model model;
    ServiceProxy localService;
    private Window_Listener windowListener;
    private Button_Listener button_listener;
    private int numeroWorker; //es num de player
    
    public Controller() throws Exception {
       // this.app = new AplicacionVista(this)
        this.view = new View(this);;

        this.model = new Model();
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);;
        view.setModel(model);
        this.init_window_listener();
        this.init_button_listener();
        //this.inicioSecion();
        this.initCBopciones();
        numeroWorker = 0;
    }

    public void validar_excepciones() throws Exception{
        if (view.obtenerUsuario().isEmpty() || view.obtenerClave().isEmpty()){
            throw new Exception("Digite Sus Credenciales");
        }
    }

    public void read(){
        try {
            validar_excepciones();
            String user = view.obtenerUsuario();
            String clave = view.obtenerClave();

            System.out.println("Iniciando " + user + clave);

            User u = new User(user, clave);
            this.login(u);
            //view.entrarJuego();
            view.entrarListaEspera();

            //localService.solicitar_numero_worker();
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }

    }

    public void entrarJuego(){
        view.entrarJuego();
        localService.solicitar_numero_worker();
        ServiceProxy.instance().solicitarTablaUsuarios();
    }

    public void salirJuego(){
        ServiceProxy.instance().salirJuego(model.getCurrentUser(), numeroWorker);
        //ServiceProxy.instance().solicitarTablaUsuarios();
        //this.limpiarInterfaz();
        view.entrarListaEspera();
        numeroWorker = 0;
    }

    public void win_easy(){
        view.lanzar_mensaje("El jugador Contrario Se Desconecto\n              Ganaste");
        this.limpiarInterfaz();
        model.getCurrentUser().setState("Espera");
        ServiceProxy.instance().uptadeWait(model.getCurrentUser());
        view.entrarListaEspera();
        numeroWorker = 0;

    }

    public void limpiarInterfaz(){
        view.getBtn0_0().setText("");
        view.getBtn1_0().setText("");
        view.getBtn2_0().setText("");
        view.getBtn0_1().setText("");
        view.getBtn1_1().setText("");
        view.getBtn2_1().setText("");
        view.getBtn0_2().setText("");
        view.getBtn1_2().setText("");
        view.getBtn2_2().setText("");
        //
        Color nuevoColor = new Color(230, 245, 255);

        view.getBtn0_0().setBackground(nuevoColor);
        view.getBtn1_0().setBackground(nuevoColor);
        view.getBtn2_0().setBackground(nuevoColor);
        view.getBtn0_1().setBackground(nuevoColor);
        view.getBtn1_1().setBackground(nuevoColor);
        view.getBtn2_1().setBackground(nuevoColor);
        view.getBtn0_2().setBackground(nuevoColor);
        view.getBtn1_2().setBackground(nuevoColor);
        view.getBtn2_2().setBackground(nuevoColor);
        view.getTxtMensajes().setText("");
    }

    public void all_to_lobby(){
        //view.lanzar_mensaje("Jugador"); // --
        view.entrarListaEspera();

    }

    public void uptadeTables(List<User> list){
        view.recargarTablaEspera(list);
    }

    public void ready(){
        model.getCurrentUser().setState("Listo");
        ServiceProxy.instance().uptade(model.getCurrentUser(), numeroWorker);
        this.limpiarInterfaz();
        ServiceProxy.instance().solicitarTablaUsuarios();
    }

    public void lanzar_solicitud(String nom1, String nom2){
        view.getTxtMensajes().setText("Player: " + nom1 + " vs " + "Player " + nom2);
        int respuesta = JOptionPane.showConfirmDialog(view, "Player: " + nom1 + " vs " + "Player " + nom2 + "\n¿Quieres Comenzar?", "Confirmación", JOptionPane.YES_NO_OPTION);

        // Verifica la respuesta del usuario
        User u = model.getCurrentUser();
        if (respuesta == JOptionPane.YES_OPTION) {
            u.setState("Jugando");
            ServiceProxy.instance().uptadeReady(u);
            entrarJuego();
        } else {
            u.setState("Espera");
            ServiceProxy.instance().uptadeWait(u);
            System.out.println("El usuario seleccionó No.");
        }
        //ServiceProxy.instance().solicitarTablaUsuarios();

    }

    public void init_window_listener(){
        windowListener = new Window_Listener(this);
        view.addWindowListener(windowListener);
    }

    public void init_button_listener(){

        button_listener = new Button_Listener(this);
        view.getBtn0_0().addActionListener(button_listener);
        view.getBtn1_0().addActionListener(button_listener);
        view.getBtn2_0().addActionListener(button_listener);
        view.getBtn0_1().addActionListener(button_listener);
        view.getBtn1_1().addActionListener(button_listener);
        view.getBtn2_1().addActionListener(button_listener);
        view.getBtn0_2().addActionListener(button_listener);
        view.getBtn1_2().addActionListener(button_listener);
        view.getBtn2_2().addActionListener(button_listener);
        view.getBtnLogIn().addActionListener(button_listener);
        view.getBtnListo().addActionListener(button_listener);
        view.getBtnStart().addActionListener(button_listener);
        view.getBtn_salir().addActionListener(button_listener);
        view.getBtn_log_out().addActionListener(button_listener);

    }

    public void login(User u){
        try {
            User logged = ServiceProxy.instance().login(u);
            model.setCurrentUser(logged);
            model.commit(Model.USER);
            ServiceProxy.instance().inicializar_servidor();
        }catch (Exception ex){
            //view.lanzar_mensaje("Ya hay 3 jugadores conectados\n");
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

    public void ponerFicha(String txt){
        try {
            Message message = new Message();
            //message.setMessage(text);
            Position obj = this.generar_ficha(txt);
            //if (model.getLista_candidatos().existAlready(obj.getId()))
                //throw new Exception("Este cantidado ya existe\n");
            message.setSender(model.getCurrentUser());
            ServiceProxy.instance().enviar_ficha(obj);
            model.commit(Model.CHAT);
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
        //view.limpiar_interfaz();
    }

    public void fichaValida(boolean r, Position obj){
       //view.lanzar_mensaje("Ficha " + txt);
        if(r==false && obj.getNumW()==numeroWorker){
            JOptionPane.showMessageDialog(view, "Posicion Seleccionada\n", "Mensaje" , JOptionPane.INFORMATION_MESSAGE);
            if (numeroWorker == 1) {
                changeTurno(2);
            } else if (numeroWorker == 2) {
               changeTurno(1);
            }
        }

        if (r && obj.getNumW()==1){
            devolverbutton(obj.getRow(), obj.getColumn()).setText("X");
            devolverbutton(obj.getRow(), obj.getColumn()).setBackground(Color.orange);
        }
        if (r && obj.getNumW()==2){
            devolverbutton(obj.getRow(), obj.getColumn()).setText("O");
            devolverbutton(obj.getRow(), obj.getColumn()).setBackground(Color.GREEN);
        }

        //view.limpiar_interfaz();
    }



    public void initCBopciones(){

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
*/


    public Position generar_ficha(String txt) {

        int column = (int) this.obtenerColumn(txt);
        int row = (int) this.obtenerRow(txt);
        //validar_excepciones();
        return new Position(row, column, "gamed", numeroWorker);
    }

    public JButton devolverbutton(int row, int colum){
        String op = "btn_"+row+"_"+colum;
        JButton btn = null;
        switch (op){
            case "btn_0_0": btn = view.getBtn0_0(); break;
            case "btn_1_0": btn = view.getBtn1_0(); break;
            case "btn_2_0": btn = view.getBtn2_0(); break;
            case "btn_0_1": btn = view.getBtn0_1(); break;
            case "btn_1_1": btn = view.getBtn1_1(); break;
            case "btn_2_1": btn = view.getBtn2_1(); break;
            case "btn_0_2": btn = view.getBtn0_2(); break;
            case "btn_1_2": btn = view.getBtn1_2(); break;
            case "btn_2_2": btn = view.getBtn2_2(); break;
            default: break;
        }
        return btn;
    }

    public int obtenerColumn(String op){
        int r= -1;
        switch (op){
            case "btn_0_0", "btn_1_0", "btn_2_0": r = 0; break;
            case "btn_0_1", "btn_1_1", "btn_2_1": r = 1; break;
            case "btn_0_2", "btn_1_2", "btn_2_2": r = 2; break;
            default: break;
        }
        return r;
    }

    public int obtenerRow(String op){
        int r= -1;
        switch (op){
            case "btn_0_0", "btn_0_1", "btn_0_2": r = 0; break;
            case "btn_1_0", "btn_1_1", "btn_1_2": r = 1; break;
            case "btn_2_0", "btn_2_1", "btn_2_2": r = 2; break;
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

    public void deliver_players(String message){
        //System.out.println("Este es mi numero de worker " + numeroWorker + "\n\n\n\n\n\n");
        if(numeroWorker!=0) {
            model.getMessages().add(message);
            //System.out.println("Se envio este mensaje: " + message);
            view.lanzar_mensaje(message);
            model.commit(Model.CHAT);
        }
        model.getCurrentUser().setState("Espera");
        ServiceProxy.instance().uptadeWait(model.getCurrentUser());
        limpiarInterfaz();
        numeroWorker = 0;
        view.entrarListaEspera();
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
            view.getBtn0_0().setEnabled(false);
            view.getBtn1_0().setEnabled(false);
            view.getBtn2_0().setEnabled(false);
            view.getBtn0_1().setEnabled(false);
            view.getBtn1_1().setEnabled(false);
            view.getBtn2_1().setEnabled(false);
            view.getBtn0_2().setEnabled(false);
            view.getBtn1_2().setEnabled(false);
            view.getBtn2_2().setEnabled(false);
        }
        if(numeroWorker==1){
            view.getBtn0_0().setEnabled(true);
            view.getBtn1_0().setEnabled(true);
            view.getBtn2_0().setEnabled(true);
            view.getBtn0_1().setEnabled(true);
            view.getBtn1_1().setEnabled(true);
            view.getBtn2_1().setEnabled(true);
            view.getBtn0_2().setEnabled(true);
            view.getBtn1_2().setEnabled(true);
            view.getBtn2_2().setEnabled(true);
        }
    }

    public void changeTurno(int numW){
        if(numeroWorker!=numW){
            view.getBtn0_0().setEnabled(true);
            view.getBtn1_0().setEnabled(true);
            view.getBtn2_0().setEnabled(true);
            view.getBtn0_1().setEnabled(true);
            view.getBtn1_1().setEnabled(true);
            view.getBtn2_1().setEnabled(true);
            view.getBtn0_2().setEnabled(true);
            view.getBtn1_2().setEnabled(true);
            view.getBtn2_2().setEnabled(true);
        }else{
            view.getBtn0_0().setEnabled(false);
            view.getBtn1_0().setEnabled(false);
            view.getBtn2_0().setEnabled(false);
            view.getBtn0_1().setEnabled(false);
            view.getBtn1_1().setEnabled(false);
            view.getBtn2_1().setEnabled(false);
            view.getBtn0_2().setEnabled(false);
            view.getBtn1_2().setEnabled(false);
            view.getBtn2_2().setEnabled(false);
        }
    }

    public void lanzar_mensaje(String msg){
        view.lanzar_mensaje(msg);
    }

    public User obtenerUser(){
        return model.getCurrentUser();
    }

}
