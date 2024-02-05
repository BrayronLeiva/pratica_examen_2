package chatServer;

import chatProtocol.*;
import chatServer.data.Data;

public class Service implements IService{

    private Data data;
    
    public Service() {
        data =  new Data();
    }
    
    public void post(Message m){
        // if wants to save messages, ex. recivier no logged on
    }

   // @Override
    public void agregar_cantidato(Candidato obj) {
        //data.getLista_candidatos().add(obj);
        //data.imprimir_candidatos();
    }

    @Override
    public void enviar_ficha(Position obj) {
        System.out.println("Se entro a agregar ficha");
        System.out.println(data.colocarFicha(obj));
        data.imprimirTablero();
    }

    public User login(User p) throws Exception{
        //for(User u:data.getUsers()) if(p.equals(u)) return u;
        //throw new Exception("User does not exist");

        //p.setNombre(p.getId());
        return p;
    }

    /*public Lista_Candidatos obtener_lista_candidatos(){
        return data.getLista_candidatos();
    }

     */

    @Override
    public void inicializar_servidor() { //solo cuando se reinicia al inicio es el cosntructor de data
        data.inicializarTablero();
    }

    @Override
    public String juegoGanado() {
        String game = data.juegoGanado();
        if (!game.equals("")){
            data.inicializarTablero();
            return game;
        }
        return "";
        //data.juegoGanado();
    }

   /* @Override
    public void efectuar_voto(String id) {
        data.efectuar_voto(id);
        data.imprimir_candidatos();
    }

    */

    public void logout(User p) throws Exception{
        //nothing to do
    }
    /*public Candidato obtener_cantidato_x_id(String id) {
        return data.seleccionar_candidato(id);
    }*/
}
