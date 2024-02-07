package chatProtocol;

import chatClient.logic.ServiceProxy;

import java.util.List;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception; 
    public void post(Message m);
   // public void agregar_cantidato(Candidato obj);
    public boolean enviar_ficha(Position obj);
   // public Lista_Candidatos obtener_lista_candidatos();
    public void inicializar_servidor();
    //public void efectuar_voto(String id);
    //public Candidato obtener_cantidato_x_id(String id);
    public String juegoGanado();
    public void solicitarTablaUsuarios();
    public ListaUsers getListaPlayers();
    public ListaUsers getListUsers();
    public String playersReady();
    public void salirJuego(User user, int i);
    //uptades
    public void uptadeAllWait();
    public void uptade(User user, int i);
    public void uptadeWait(User user);
    public void uptadeReady(User user);


}
