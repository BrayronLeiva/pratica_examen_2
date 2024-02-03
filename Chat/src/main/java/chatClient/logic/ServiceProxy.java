package chatClient.logic;

import chatClient.presentation.Controller.Controller;
import chatProtocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    @Override
    public void agregar_cantidato(Candidato obj) {
        try {
            out.writeInt(Protocol.ADD_CANTIDATO);
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {

        }
    }

    @Override
    public void efectuar_voto(String id) {
        try {
            out.writeInt(Protocol.VOTO_EFECTUADO);
            out.writeObject(id);
            out.flush();
        } catch (IOException ex) {

        }
    }

    @Override
    public Candidato obtener_cantidato_x_id(String id) {    return null;}

    @Override
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
                } //switch
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }
   private void deliver( final String message ){
      SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               controller.deliver(message);
            }
         }
      );
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

}
