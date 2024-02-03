
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
    
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
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
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream() );
                System.out.println("Conexion Establecida...");
                User user=this.login(in,out,service);                          
                Worker worker = new Worker(this,in,out,user, service); 
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
        user=service.login(user);
        out.writeInt(Protocol.ERROR_NO_ERROR);
        out.writeObject(user);
        out.flush();
        return user;
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
    }

    public void uptade_candidato_lista_clientes(Candidato obj){
        for(Worker wk:workers){
            wk.uptade_candidato_lista_clientes(obj);
        }
    }
    
}