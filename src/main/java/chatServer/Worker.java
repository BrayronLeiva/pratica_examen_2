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
    int numeroPlayer;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service, int num) {
        this.srv=srv;
        this.in=in;
        this.out=out;
        this.user=user;
        this.service=service;
        this.numeroWorker =num;
        this.numeroPlayer = 0;
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
                        User u = (User) in.readObject();
                        srv.remove(user);
                        service.logout(u);
                        ListaUsers users = service.getListUsers();

                        ListaUsers newList = new ListaUsers();
                        for (User obj:users.getUsers()) {
                            newList.getUsers().add(new User(obj.getNombre(), obj.getClave(), obj.getState()));
                        }

                        srv.send_lista_users(newList); //ya que es para cada usuario
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
                            srv.current_numero_player+=1;
                            numeroPlayer = srv.current_numero_player;
                            this.send_numero_worker(numeroPlayer); //ya que es para cada usuario
                        } catch (Exception ex) {}
                        break;
                    case Protocol.ENVIAR_FICHA:
                        try {
                            if(srv.getCurrent_numero_player()<2){
                                this.deliver("Deben De Haber 2 Jugadores Conectado Para Jugar\n");
                                Position obj = (Position) in.readObject();
                            }else {

                                System.out.println("Se tiene que agregar una ficha");
                                Position obj = (Position) in.readObject();
                                //System.out.println(obj.getState());
                                boolean r = service.enviar_ficha(obj);
                                srv.fichaCorrecta(r, new Position(obj.getRow(), obj.getColumn(),obj.getState(), obj.getNumW()));
                                String game = service.juegoGanado();
                                if (!game.equals("")) {
                                    srv.current_numero_player=0;
                                    service.inicializar_servidor();
                                    srv.enviar_ganador(game);
                                }
                                srv.sendPlayerPlayed(obj.getNumW());

                                //srv.add_candidato_lista_clientes(new Candidato(obj.getId(), obj.getNombre(),obj.getVotos()));
                            }
                        } catch (Exception ex) {}
                        break;
                    case Protocol.REQUEST_LISTA_USERS:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            ListaUsers users = service.getListUsers();

                            ListaUsers newList = new ListaUsers();
                            for (User obj:users.getUsers()) {
                                newList.getUsers().add(new User(obj.getNombre(), obj.getClave(), obj.getState()));
                            }

                            srv.send_lista_users(newList); //ya que es para cada usuario
                        } catch (Exception ex) {}
                        break;
                    case Protocol.UPTADE_LISTA_USERS:
                        try {
                            if(srv.getCurrent_numero_player()>=2){
                                this.deliver("La Sala De Juego Esta Ocupada\nEspera tu turno");
                                User obj = (User) in.readObject();
                            }
                            //srv.send_numero_worker(numeroWorker);
                            User obj = (User) in.readObject();
                            service.uptade(obj, 0);
                            String r = service.playersReady();
                            if (!r.equals("")){
                                String[] names = r.split("-");
                                String nombre1 = names[0];
                                String nombre2 = names[1];
                                srv.lanzarPartida(nombre1,nombre2);
                            }
                            //ListaUsers users = service.getListaPlayers();
                            //srv.send_lista_users(users); //ya que es para cada usuario
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case Protocol.UPTADE_WAIT_LISTA_USERS:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            User obj = (User) in.readObject();
                            service.uptadeWait(obj);
                            //ListaUsers users = service.getListaPlayers();
                            //srv.send_lista_users(users); //ya que es para cada usuario
                        } catch (Exception ex) {}
                        break;
                    case Protocol.UPTADE_READY_LISTA_USERS:
                        try {

                            User obj = (User) in.readObject();
                            service.uptadeReady(obj);

                        } catch (Exception ex) {}
                        break;
                    case Protocol.SALIR_JUEGO:
                        try {
                            //srv.send_numero_worker(numeroWorker);
                            User obj = (User) in.readObject();
                            service.uptadeWait(obj);
                            //service.uptadeAllWait();
                            System.out.println("Mi numero de Player es " + numeroPlayer);
                            int winner = 0;
                            if(srv.current_numero_player>=2) {
                                if (numeroPlayer == 1) {
                                    srv.win_easy(2);
                                    winner = 2;
                                } else if (numeroPlayer == 2) {
                                    srv.win_easy(1);
                                    winner =1;
                                }
                            }
                            service.inicializar_servidor(); //inicializa tablero
                            srv.current_numero_player=0;
                            srv.resetUI(winner);
                            //srv.all_to_lobby();



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
    public void enviar_ganador(String message){ //deliver personalizado
        try {
            out.writeInt(Protocol.DELIVER_PLAYERS);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void resetUI(){
        try {
            out.writeInt(Protocol.RESET_UI);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void win_easy(){
        try {
            out.writeInt(Protocol.WIN_EASY);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void all_to_lobby(){
        try {
            out.writeInt(Protocol.ALL_TO_LOBBY);
            out.flush();
        } catch (IOException ex) {
        }
    }


    public void fichaCorrecta(boolean r, Position obj){
        try {
            out.writeInt(Protocol.FICHA_CORRECTA);
            out.writeObject(r);
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {
        }
    }
    public void lanzarPartida(String nom1, String nom2){
        try {
            out.writeInt(Protocol.LANZAR_PARTIDA);
            out.writeObject(nom1);
            out.writeObject(nom2);
            out.flush();
        } catch (IOException ex) {
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

    public User getUser() {return user;}
}
