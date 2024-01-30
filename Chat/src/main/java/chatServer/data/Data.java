package chatServer.data;

import chatProtocol.Lista_Candidatos;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<User> users;
    private Lista_Candidatos lista_candidatos;

    public Data() {
        users = new ArrayList<>();
        lista_candidatos = new Lista_Candidatos();

    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Lista_Candidatos getLista_candidatos() {
        return lista_candidatos;
    }

    public void setLista_candidatos(Lista_Candidatos lista_candidatos) {
        this.lista_candidatos = lista_candidatos;
    }
}
