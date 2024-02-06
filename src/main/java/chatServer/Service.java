package chatServer;

import chatProtocol.*;
import chatServer.data.Data;

import java.util.List;

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
    public boolean enviar_ficha(Position obj) {
        System.out.println("Se entro a agregar ficha");
        boolean r = data.colocarFicha(obj);
        data.imprimirTablero();
        return r;
    }

    public User login(User p) throws Exception{
        for(User u:data.getUsers().getUsers()) if(p.getNombre().equals(u.getNombre())&&p.getClave().equals(u.getClave())) {
            System.out.println("Ingresando Usuario "+ u.getNombre() + " " + u.getClave());

            for (User e:data.getPlayers().getUsers()) {
                if (e.getNombre().equals(p.getNombre())){
                    throw new Exception("Este jugador se encuentra ya logeado\n");
                }
            }
            data.getPlayers().getUsers().add(new User(p.getNombre(), p.getClave(), "Espera"));

            u.setState("Espera");
            return p;
        }
        throw new Exception("Este usuario No Esta Registrado");

        //p.setNombre(p.getId());
        //return p;
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

    @Override
    public void solicitarTablaUsuarios() {}
    @Override
    public ListaUsers getListaPlayers(){

        //System.out.println("TAMANO " + data.getPlayers().size());
        //System.out.println(data.getPlayers().size());

        return  data.getPlayers();
        //return data.getPlayers();
    }

    @Override
    public void uptadeAllWait() {
        data.uptade_all_wait();
    }

    public ListaUsers getListUsers(){
        //data.imprimirListaUsuarios();
        data.imprimirListaPlayers();
        return data.getUsers();
        //return data.cloneListUsers();

    }

    @Override
    public void uptade(User user, int i) {
        data.uptade(user, i);
    }

   /* @Override
    public void efectuar_voto(String id) {
        data.efectuar_voto(id);
        data.imprimir_candidatos();
    }

    */

    public void logout(User p) throws Exception{
        data.removePlayer(p);
        data.uptade_down(p);
    }
    @Override
    public void uptadeWait(User u){
        data.uptade_wait(u);
    }
    @Override
    public void uptadeReady(User user){
        data.uptade_ready(user);
    }
    @Override
    public String playersReady(){

        String r =  data.players_ready();
        System.out.println("LO QUE VOY A MANDAR: " + r);
        if(!r.equals("")) {return r;}
        return "";
    }

    @Override
    public void salirJuego(User user, int i) {}

    /*public Candidato obtener_cantidato_x_id(String id) {
        return data.seleccionar_candidato(id);
    }*/
}
