package chatServer;

import chatProtocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Worker {
    Server srv;
    ObjectInputStream in;
    ObjectOutputStream out;
    IService service;
    User user;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service) {
        this.srv=srv;
        this.in=in;
        this.out=out;
        this.user=user;
        this.service=service;
    }

    boolean continuar;    
    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    //inicializar_cliente();
                    listen();

                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {  
        }
    }
    
    public void stop(){
        continuar=false;
        System.out.println("Conexion cerrada...");
    }
    public void inicializar_cliente(){
        try {
            int r = in.readInt();
            if (Protocol.SEND_LISTA_CANTIDADOS == r) {
                out.writeObject(service.obtener_lista_candidatos());
                out.flush();
            }
        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }
    public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("Operacion: "+method);
                switch(method){
                //case Protocol.LOGIN: done on accept
                case Protocol.LOGOUT:
                    try {
                        srv.remove(user);
                        //service.logout(user); //nothing to do
                    } catch (Exception ex) {}
                    stop();
                    break;
                    case Protocol.ADD_CANTIDATO:
                        try {
                            System.out.println("Se tiene que agregar un candidato");
                            Candidato obj = (Candidato) in.readObject();
                            System.out.println(obj.getNombre());
                            service.agregar_cantidato(obj);
                            srv.add_candidato_lista_clientes(obj);
                        } catch (Exception ex) {}
                        break;
                    case Protocol.SEND_LISTA_CANTIDADOS:
                        try {
                            Lista_Candidatos list = service.obtener_lista_candidatos();
                            srv.set_lista_candidatos_clientes(list);
                        } catch (Exception ex) {}
                        break;
                    case Protocol.POST:
                    Message message=null;
                    try {
                        //message = (Message)in.readObject();
                        //message.setSender(user);
                        //srv.deliver(message);
                        //service.post(message); // if wants to save messages, ex. recivier no logged on
                        //System.out.println(user.getNombre()+": "+message.getMessage());
                    } catch (Exception ex) {}
                    break;
                    case Protocol.VOTO_EFECTUADO:
                        //Message message=null;
                        try {
                            String id = (String) in.readObject();
                            service.efectuar_voto(id);
                            Candidato obj = service.obtener_cantidato_x_id(id);
                            System.out.println("Votos de candidato que envio " + obj.getVotos());
                            srv.uptade_candidato_lista_clientes(new Candidato(obj.getId(),obj.getNombre(),obj.getVotos()));
                        } catch (Exception ex) {}
                        break;

                }
                out.flush();
            } catch (IOException  ex) {
                System.out.println(ex);
                continuar = false;
            }                        
        }
    }
    
    public void deliver(String message){
        try {
            out.writeInt(Protocol.DELIVER);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void add_candidato_lista_clientes(Candidato obj){
       try {
           out.writeInt(Protocol.ADD_CANTIDATO_LISTA);
           out.writeObject(obj);
           out.flush();
       }catch (Exception ex){
           System.out.println("Excepcion: " + ex.getMessage());
       }
    }

    public void set_lista_candidatos_clientes(Lista_Candidatos list){
        try {
            out.writeInt(Protocol.INIT_LISTA_CANTIDATOS);
            out.writeObject(list);
            out.flush();
        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }

    public void uptade_candidato_lista_clientes(Candidato obj){
        try {
            out.writeInt(Protocol.UPTADE_CANTIDATO_LISTA);
            out.writeObject(obj);
            out.flush();
        }catch (Exception ex){
            System.out.println("Excepcion: " + ex.getMessage());
        }
    }
}
