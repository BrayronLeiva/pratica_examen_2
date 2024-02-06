package chatProtocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaUsers implements Serializable {
    private List<User> users;

    public ListaUsers() {
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }
}
