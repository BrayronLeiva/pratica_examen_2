
package chatServer;

import chatProtocol.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

public class Server {
    ServerSocket srv;
    List<Worker> workers;
    int current_num_worker;
    
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
            current_num_worker = 0;
        } catch (IOException ex) {
        }
    }
    
    public void run(){
        IService service = new Service();

        boolean continuar = true;
        ObjectInputStream in=null;
        ObjectOutputStream out=null;
        Socket skt=null;
        while (continuar) {
            try {
                //skt = srv.accept();
                skt = srv.accept();
                System.out.println("Se esta agregando un worker");
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream() );

                current_num_worker+=1;
                if (current_num_worker>2){
                    try {
                        out.writeInt(Protocol.DELIVER);
                        out.writeObject("Ya hay 2 jugadores Conectados\n");
                        out.flush();
                    } catch (Exception ex) {}
                    continuar =false;
                    break;
                }

                System.out.println("Conexion Establecida...");
                User user=this.login(in,out,service);

                Worker worker = new Worker(this,in,out,user, service, current_num_worker);
                workers.add(worker);                      
                worker.start();
                //worker.inicializar_cliente();
            }
            catch (IOException | ClassNotFoundException ex) {}
            catch (Exception ex) {
                try {
                    out.writeInt(Protocol.ERROR_LOGIN);
                    out.flush();
                    skt.close();
                } catch (IOException ex1) {}
               System.out.println("Conexion cerrada...");
            }
        }
    }
    
    private User login(ObjectInputStream in,ObjectOutputStream out,IService service) throws IOException, ClassNotFoundException, Exception{
        int method = in.readInt();
        if (method!=Protocol.LOGIN) throw new Exception("Should login first");
        System.out.println("Comenzando Login");
        User user=(User)in.readObject();                          
        user=service.login(user);;
        out.writeInt(Protocol.ERROR_NO_ERROR);
        out.writeObject(user);
        out.flush();
        return user;
    }

    public void sendPlayerPlayed(int numeroWorker){
        for(Worker wk:workers){
            wk.sendPlayerPlayed(numeroWorker);
        }
    }
    
    public void deliver(String message){
        for(Worker wk:workers){
            wk.deliver(message);
        }        
    }

    public void add_candidato_lista_clientes(Candidato obj){
        for(Worker wk:workers){
            wk.add_candidato_lista_clientes(obj);
        }
    }

    public void set_lista_candidatos_clientes(Lista_Candidatos list){
        for(Worker wk:workers){
            wk.set_lista_candidatos_clientes(list);
        }
    }
    
    public void remove(User u){
        for(Worker wk:workers) if(wk.user.equals(u)){workers.remove(wk);break;}
        System.out.println("Quedan: " + workers.size());
        reasignar();
    }

    public void reasignar(){
        if(workers.size()==1) {
            workers.get(0).setNumeroWorker(1);
            workers.get(0).send_numero_worker(1);
            workers.get(0).deliver("Jugador Se Desconecto - Reiniciando Juego\n");
            current_num_worker = 1;
        }
        if(workers.size()==0){
            current_num_worker = 0;
        }
    }

    public void uptade_candidato_lista_clientes(Candidato obj){
        for(Worker wk:workers){
            wk.uptade_candidato_lista_clientes(obj);
        }
    }

    public void  send_lista_users(ListaUsers list){
        for(Worker wk:workers){
            wk.send_lista_users(list);
        }
    }

    public int getCurrent_num_worker() {
        return current_num_worker;
    }
}