package chatProtocol;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception; 
    public void post(Message m);
    public void agregar_cantidato(Candidato obj);
    public Lista_Candidatos obtener_lista_candidatos();
    public void inicializar_servidor();
    public void efectuar_voto(String id);
    public Candidato obtener_cantidato_x_id(String id);

}
