package chatClient.logic;

import chatClient.presentation.Controller.Controller;
import chatProtocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class ServiceProxy implements IService{
    private static IService theInstance;
    public static IService instance(){
        if (theInstance==null){ 
            theInstance=new ServiceProxy();
        }
        return theInstance;
    }

    ObjectInputStream in;
    ObjectOutputStream out;
    Controller controller;

    public ServiceProxy() {           
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    Socket skt;
    private void connect() throws Exception{
        skt = new Socket(Protocol.SERVER,Protocol.PORT);
        out = new ObjectOutputStream(skt.getOutputStream() );
        out.flush();
        in = new ObjectInputStream(skt.getInputStream());
        System.out.println("Se ejecuta la conexion");
    }

    private void disconnect() throws Exception{
        skt.shutdownOutput();
        skt.close();
    }

    public void inicializar_servidor(){
        try {
            out.writeInt(Protocol.SEND_LISTA_CANTIDADOS);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    public void salirJuego(User user, int nW){
        try {
            out.writeInt(Protocol.SALIR_JUEGO);
            out.writeObject(user);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    public void solicitarTablaUsuarios(){
        try {
            out.writeInt(Protocol.REQUEST_LISTA_USERS);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }
    @Override
    public void uptade(User user, int i){
        try {
            out.writeInt(Protocol.UPTADE_LISTA_USERS);
            out.writeObject(user);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }
    @Override
    public void uptadeReady(User user){
        try {
            out.writeInt(Protocol.UPTADE_READY_LISTA_USERS);
            out.writeObject(user);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    @Override
    public void uptadeWait(User user) {
        try {
            out.writeInt(Protocol.UPTADE_WAIT_LISTA_USERS);
            out.writeObject(user);
            out.flush();

        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    @Override
    public ListaUsers getListUsers() {
        return null;
    }

    @Override
    public String playersReady() {
        return null;
    }

    @Override
    public ListaUsers getListaPlayers() {
        return null;
    }

    @Override
    public void uptadeAllWait() {}

    @Override
    public String juegoGanado() {
        return "";
    }

    public User login(User u) throws Exception{
        connect();
        try {
            out.writeInt(Protocol.LOGIN);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();
            if (response==Protocol.ERROR_NO_ERROR){
                User u1=(User) in.readObject();
                this.start();
                //this.inicializar_servidor();
                return u1;
            }
            else {
                String issue = (String) in.readObject();
                controller.lanzar_mensaje(issue);
                disconnect();
                throw new Exception("No remote user");
            }            
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
    
    public void logout(User u) throws Exception{
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }
    
    public void post(Message message){
        try {
            out.writeInt(Protocol.POST);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
            
        }   
    }

    public void solicitar_numero_worker() {
        try {
            out.writeInt(Protocol.REQUEST_NUMERO_WORKER);
            out.flush();

        } catch (Exception ex) {
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    //@Override
    public void agregar_cantidato(Candidato obj) {
        try {
            out.writeInt(Protocol.ADD_CANTIDATO);
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {

        }
    }

    @Override
    public boolean enviar_ficha(Position obj) {
        try {
            out.writeInt(Protocol.ENVIAR_FICHA);
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {

        }
        return true;
    }

    //@Override
    public void efectuar_voto(String id) {
        try {
            out.writeInt(Protocol.VOTO_EFECTUADO);
            out.writeObject(id);
            out.flush();
        } catch (IOException ex) {

        }
    }

    //@Override
    public Candidato obtener_cantidato_x_id(String id) {    return null;}

    //@Override
    public Lista_Candidatos obtener_lista_candidatos() {
        return null;
    }

    // LISTENING FUNCTIONS
   boolean continuar = true;    
   public void start(){
        System.out.println("Client worker atendiendo peticiones...");
        Thread t = new Thread(new Runnable(){
            public void run(){
                listen();
            }
        });
        continuar = true;
        t.start();
    }
    public void stop(){
        continuar=false;
    }
    
   public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("DELIVERY");
                System.out.println("Operacion: "+method);
                switch(method){
                case Protocol.DELIVER: {
                    try {
                        String message = (String) in.readObject();
                        deliver(message);
                    } catch (ClassNotFoundException ex) {
                    }
                    break;
                }
                case Protocol.ADD_CANTIDATO_LISTA: {
                    System.out.println("Agregando candidato a mi lista propia");
                    try {
                        Candidato obj = (Candidato) in.readObject();
                        agregar_candidato_lista(obj);

                    } catch (Exception ex) {
                    }
                    break;
                }
                case Protocol.UPTADE_CANTIDATO_LISTA: {
                        System.out.println("Actualizando candidato a mi lista propia");
                        try {
                            Candidato obj = (Candidato) in.readObject();
                            actualizar_candidato_lista(obj);

                        } catch (Exception ex) {
                        }
                        break;
                    }
                case Protocol.INIT_LISTA_CANTIDATOS: {
                    System.out.println("Agregando candidato a mi lista propia");
                    try {
                        Lista_Candidatos list = (Lista_Candidatos) in.readObject();
                        iniciar_candidato_lista(list);
                    } catch (Exception ex) {
                        System.out.println("Excepcion: " + ex.getMessage());
                    }
                    break;
                }
                case Protocol.SEND_NUMERO_WORKER: {
                    try {
                        System.out.println("Seteando worker\n\n\n\n\n\n\n");
                        int numeroWorker = (int) in.readInt();
                        set_numero_worker(numeroWorker);
                    } catch (Exception ex) {
                        System.out.println("Excepcion: " + ex.getMessage());
                    }
                    break;
                }
                case Protocol.SEND_PLAYER_PLAYED: {
                    try {
                        System.out.println("Cambiando worker player\n");
                        int numeroWorker = (int) in.readInt();
                        sendPlayerPlayed(numeroWorker);
                    } catch (Exception ex) {
                        System.out.println("Excepcion: " + ex.getMessage());
                    }
                    break;
                }
                case Protocol.SEND_LISTA_USERS: {
                    try {
                        System.out.println("Seteando worker\n\n\n\n\n\n\n");
                        ListaUsers list;
                        list= (ListaUsers) in.readObject();
                        //System.out.println(list.get(0).getState() + list.get(0).getNombre() + " IIII");

                        uptadeListaUser(list);
                    } catch (Exception ex) {
                        System.out.println("Excepcion: " + ex.getMessage());
                    }
                    break;
                }
                    case Protocol.LANZAR_PARTIDA: {
                        try {
                            String nom1 = (String) in.readObject();
                            String nom2 = (String) in.readObject();
                            lanzarPartida(nom1, nom2);
                        } catch (Exception ex) {
                            System.out.println("Excepcion: " + ex.getMessage());
                        }
                        break;
                    }
                    case Protocol.FICHA_CORRECTA: {
                        try {
                           boolean r= (boolean) in.readObject();
                           Position obj = (Position) in.readObject();
                            fichaValida(r, obj);
                        } catch (Exception ex) {
                            System.out.println("Excepcion: " + ex.getMessage());
                        }
                        break;
                    }
                    case Protocol.ALL_TO_LOBBY: {
                        try {
                            this.all_to_lobby();

                        } catch (Exception ex) {
                            System.out.println("Excepcion: " + ex.getMessage());
                        }
                        break;
                    }
                    case Protocol.WIN_EASY: {
                        try {
                            this.win_easy();

                        } catch (Exception ex) {
                            System.out.println("Excepcion: " + ex.getMessage());
                        }
                        break;
                    }
                    case Protocol.RESET_UI: {
                        try {
                            this.reset_ui();

                        } catch (Exception ex) {
                            System.out.println("Excepcion: " + ex.getMessage());
                        }
                        break;
                    }
                    case Protocol.DELIVER_PLAYERS: {
                        try {
                            String message = (String) in.readObject();
                            deliver_players(message);
                        } catch (ClassNotFoundException ex) {
                        }
                        break;
                    }

                } //switch
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }

    private void deliver_players( final String message ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                                           controller.deliver_players(message);
                                       }
        });
    }

    private void reset_ui(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                controller.limpiarInterfaz();
            }
        });
    }

    private void win_easy(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {

                controller.win_easy();
            }          }

        );
    }

   private void deliver( final String message ){
      SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               controller.deliver(message);
            }
         }
      );
   }
    private void all_to_lobby( ){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.all_to_lobby();
                                       }
                                   }
        );
    }

    private void fichaValida( final boolean r, final Position obj){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.fichaValida(r, obj);
                                       }
                                   }
        );
    }

    private void lanzarPartida( final String nom1, final String nom2 ){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.lanzar_solicitud(nom1, nom2);
                                       }
                                   }
        );
    }

    private void sendPlayerPlayed( final int numW ){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.changeTurno(numW);
                                       }
                                   }
        );
    }

    private void set_numero_worker(final int num) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                controller.setNumeroWorker(num);
            }
        });
    }

    private void agregar_candidato_lista( final Candidato obj ){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.agregar_candidato_lista(obj);
                                       }
                                   }
        );
    }

    private void iniciar_candidato_lista( final Lista_Candidatos list ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                controller.iniciar_candidato_lista(list);
            }
        }
        );
   }

    private void actualizar_candidato_lista( Candidato obj ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                controller.actualizar_candidato_lista(obj);
            }
        });
    }

    private void uptadeListaUser(final ListaUsers list ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                controller.uptadeTables(list.getUsers());
            }
        }
        );
    }


}
