
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
    int current_numero_player;
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
            current_num_worker = 0;
            current_numero_player = 0;
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
                /*if (current_num_worker>2){
                    try {
                        out.writeInt(Protocol.DELIVER);
                        out.writeObject("Ya hay 2 jugadores Conectados\n");
                        out.flush();
                    } catch (Exception ex) {}
                    continuar =false;
                    break;
                }*/

                System.out.println("Conexion Establecida...");
                User user=this.login(in,out,service);

                Worker worker = new Worker(this,in,out,user, service, current_num_worker);
                workers.add(worker);                      
                worker.start();
                //worker.inicializar_cliente();
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            catch (Exception ex) {
                try {
                    System.out.println(ex.getMessage());
                    out.writeInt(Protocol.ERROR_LOGIN);
                    out.writeObject(ex.getMessage());
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

    public void enviar_ganador(String message){
        for(Worker wk:workers){
            if (wk.numeroPlayer==1 || wk.numeroPlayer==2){
                wk.enviar_ganador(message);
            }
        }
    }

    public void resetUI(int nump){
        for(Worker wk:workers){
            if(wk.numeroPlayer!=nump){
                wk.resetUI();
            }

        }
    }

    public void all_to_lobby(){
        for(Worker wk:workers){
            wk.all_to_lobby();
        }
    }

    public void fichaCorrecta(boolean r, Position obj){
        for(Worker wk:workers){
            wk.fichaCorrecta(r,obj);
        }
    }

    public void lanzarPartida(String nom1, String nom2){

        for(Worker wk:workers){
            if(wk.getUser().getNombre().equals(nom1) || wk.getUser().getNombre().equals(nom2)) {
                wk.lanzarPartida(nom1, nom2);
            }
        }
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

    
    public void remove(User u){
        for(Worker wk:workers)
            if(wk.user.getNombre().equals(u.getNombre())){
                workers.remove(wk);break;
            }
        System.out.println("Quedan: " + workers.size());
        //reasignar();
    }

    public void win_easy(int nPl){
        for(Worker wk:workers){
            if(wk.numeroPlayer==nPl){
                wk.win_easy();
            }
        }
    }
/*
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

 */


    public void  send_lista_users(ListaUsers list){
        for(Worker wk:workers){
            wk.send_lista_users(list);
        }
    }

    public int getCurrent_num_worker() {
        return current_num_worker;
    }

    public int getCurrent_numero_player() {
        return current_numero_player;
    }
}