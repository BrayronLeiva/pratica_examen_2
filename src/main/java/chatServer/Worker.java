package chatServer;

import chatProtocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Worker {
    Server srv;
    ObjectInputStream in;
    ObjectOutputStream out;
    IService service;
    User user;
    int numeroWorker;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service, int num) {
        this.srv=srv;
        this.in=in;
        this.out=out;
        this.user=user;
        this.service=service;
        this.numeroWorker =num;
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
                //out.writeObject(service.obtener_lista_candidatos());
                //out.flush();
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
                        service.inicializar_servidor();
                        //service.logout(user); //nothing to do
                    } catch (Exception ex) {}
                    stop();
                    break;
                    case Protocol.ADD_CANTIDATO:
                        try {
                            System.out.println("Se tiene que agregar un candidato");
                            Candidato obj = (Candidato) in.readObject();
                            System.out.println(obj.getNombre());
                            //service.agregar_cantidato(obj);
                            srv.add_candidato_lista_clientes(new Candidato(obj.getId(), obj.getNombre(),obj.getVotos()));
                        } catch (Exception ex) {}
                        break;
                    case Protocol.SEND_LISTA_CANTIDADOS:
                        try {

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
                            //service.efectuar_voto(id);
                            //Candidato obj = service.obtener_cantidato_x_id(id);
                            //System.out.println("Votos de candidato que envio " + obj.getVotos());
                            //srv.uptade_candidato_lista_clientes(new Candidato(obj.getId(),obj.getNombre(),obj.getVotos()));
                        } catch (Exception ex) {}
                        break;
                    case Protocol.REQUEST_NUMERO_WORKER:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            this.send_numero_worker(numeroWorker); //ya que es para cada usuario
                        } catch (Exception ex) {}
                        break;
                    case Protocol.ENVIAR_FICHA:
                        try {
                            if(srv.getCurrent_num_worker()<2){
                                srv.deliver("Deben De Haber 2 Jugadores Conectado Para Jugar\n");
                                Position obj = (Position) in.readObject();
                            }else {

                                System.out.println("Se tiene que agregar una ficha");
                                Position obj = (Position) in.readObject();
                                //System.out.println(obj.getState());
                                service.enviar_ficha(obj);
                                String game = service.juegoGanado();
                                if (!game.equals("")) {
                                    srv.deliver(game);
                                }
                                srv.sendPlayerPlayed(obj.getNumW());

                                //srv.add_candidato_lista_clientes(new Candidato(obj.getId(), obj.getNombre(),obj.getVotos()));
                            }
                        } catch (Exception ex) {}
                        break;
                    case Protocol.REQUEST_LISTA_USERS:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            ListaUsers users = service.getListaPlayers();
                            System.out.println("Imprimiendo Antes De Enviar");
                            for (User obj: users.getUsers()) {
                                System.out.println(obj.getNombre() + " " + obj.getState());
                            }
                            ListaUsers newList = new ListaUsers();
                            for (User obj:users.getUsers()) {
                                newList.getUsers().add(new User(obj.getNombre(), obj.getClave(), obj.getState()));
                            }

                            srv.send_lista_users(newList); //ya que es para cada usuario
                        } catch (Exception ex) {}
                        break;
                    case Protocol.UPTADE_LISTA_USERS:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            User obj = (User) in.readObject();
                            service.uptade(obj, 0);
                            //ListaUsers users = service.getListaPlayers();
                            //srv.send_lista_users(users); //ya que es para cada usuario
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
    public void send_numero_worker(int numeroWorker){
        try {
            System.out.println("Se entro al deliver del worker");
            out.writeInt(Protocol.SEND_NUMERO_WORKER);//oiga estoyenviando un deliver
            out.writeInt(numeroWorker);
            out.flush();
            //aqui entrega solo a su propio cliente sin tener que propagar a todos
        } catch (IOException ex) {
        }
    }

    public void send_lista_users(ListaUsers list){
        try {
            System.out.println("Se entro al deliver lista del worker");
            out.writeInt(Protocol.SEND_LISTA_USERS);//oiga estoyenviando un deliver
            out.writeObject(list);
            out.flush();
            //aqui entrega solo a su propio cliente sin tener que propagar a todos
        } catch (IOException ex) {
        }
    }

    public void sendPlayerPlayed(int numeroWorker){
        try {
            System.out.println("Se entro al deliver del worker");
            out.writeInt(Protocol.SEND_PLAYER_PLAYED);//oiga estoyenviando un deliver
            out.writeInt(numeroWorker);
            out.flush();
            //aqui entrega solo a su propio cliente sin tener que propagar a todos
        } catch (IOException ex) {
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

    public int getNumeroWorker() {
        return numeroWorker;
    }

    public void setNumeroWorker(int numeroWorker) {
        this.numeroWorker = numeroWorker;
    }
}
