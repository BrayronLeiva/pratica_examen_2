package chatServer.data;

import chatProtocol.*;

import java.util.ArrayList;
import java.util.List;

public class Data {

    ListaUsers users;
    ListaUsers players;


    // Declarar una matriz de enteros con tamaño 3x3
    private Position[][] tablero;


    public Data() {
        users = new ListaUsers();
        players = new ListaUsers();
        this.cargarUsuarios();
        tablero = new Position[3][3];
        this.inicializarTablero();
        this.imprimirTablero();

    }

    public void cargarUsuarios(){
        users.getUsers().add(new User("chepe", "pass0001", "Inactivo"));
        users.getUsers().add(new User( "mia", "pass0002", "Inactivo"));
        users.getUsers().add(new User( "juanchi", "pass0003", "Inactivo"));
        users.getUsers().add(new User( "pedrosky", "pass0004", "Inactivo"));
        users.getUsers().add(new User( "lito", "pass0005", "Inactivo"));
        users.getUsers().add(new User("celita", "pass0001", "Inactivo"));
        users.getUsers().add(new User( "betto", "pass0006", "Inactivo"));
        users.getUsers().add(new User("silvis", "pass0007", "Inactivo"));
    }

    public void inicializarTablero(){
        for (int i = 0; i<3; i++){
            for (int e = 0; e<3; e++){
                //System.out.println("Se agrego en" + i + e + "\t");
                tablero[e][i] = new Position(e, i,"vacio");
            }
            //System.out.println("Salto\n");
        }

    }

    public void imprimirListaUsuarios(){
        System.out.println("IMPRIMIENDO LISTA DE USUARIOS DESDE DATA");
        for (User obj:users.getUsers()) {
            System.out.println(obj.getNombre() + " " + obj.getClave() + " " + obj.getState());
        }
    }

    public void imprimirListaPlayers(){
        System.out.println("IMPRIMIENDO LISTA DE PLAYERS DESDE DATA");
        for (User obj:players.getUsers()) {
            System.out.println(obj.getNombre() + " " + obj.getClave() + " " + obj.getState());
        }
    }

    public void imprimirTablero(){
        for (int i = 0; i<3; i++){
            for (int e = 0; e<3; e++){
                System.out.print(tablero[i][e].getState() +tablero[i][e].getNumW() + " ");
            }
            System.out.println("\n");
        }
    }

    public ListaUsers getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        //this.users = users;
    }

    /*public Lista_Candidatos getLista_candidatos() {
        return lista_candidatos;
    }

    public void setLista_candidatos(Lista_Candidatos lista_candidatos) {
        this.lista_candidatos = lista_candidatos;
    }
*/
    public boolean colocarFicha(Position obj){

        System.out.println(obj.getState());
        int colum = obj.getColumn();
        int row = obj.getRow();

        if(!tablero[row][colum].getState().equals("gamed")){
            tablero[row][colum] = obj;
            return true;
        }
        System.out.println("YA ESE ESPACIO ESTA OCUPADO----------------------------\n");
        return false;
    }

    public String juegoGanado(){
        String juegoH = this.juegoGanadoHorizontal();
        String juegoV = this.juegoGanadoVertical();
        if (!juegoH.equals("")){ return  juegoH;}
        if (!juegoV.equals("")){ return  juegoV;}
        return "";
    }

    public String juegoGanadoHorizontal(){
        int player1 = 0;
        int player2 = 0;

        for (int i = 0; i<3; i++){
            for (int e = 0; e<3; e++){
                if(tablero[i][e].getState().equals("gamed") && tablero[i][e].getNumW()==1){
                    player1++;
                    player2 = 0;
                } else if (tablero[i][e].getState().equals("gamed") && tablero[i][e].getNumW()==2) {
                    player2++;
                    player1 = 0;
                }
                if (player1==3){
                    return "Player 1 Gano\n";
                } else if (player2==3) {
                    return "Plater 2 Gano\n";
                }
            }
            player1 = 0;
            player2 = 0;
        }
        return "";
    }

    public String juegoGanadoVertical(){
        int player1 = 0;
        int player2 = 0;

        for (int i = 0; i<3; i++){
            for (int e = 0; e<3; e++){
                if(tablero[e][i].getState().equals("gamed") && tablero[e][i].getNumW()==1){
                    player1++;
                    player2 = 0;
                } else if (tablero[e][i].getState().equals("gamed") && tablero[e][i].getNumW()==2) {
                    player2++;
                    player1 = 0;
                }
                if (player1==3){
                    return "Player 1 Gano\n";
                } else if (player2==3) {
                    return "Plater 2 Gano\n";
                }
            }
            player1 = 0;
            player2 = 0;
        }
        return "";
    }

    public ListaUsers getPlayers() {
        return players;
    }
    public void uptade(User user, int i){
        System.out.println("ESTADO A AGREGAR " + user.getState());
        for (User obj:users.getUsers()) {
            if (obj.getNombre().equals(user.getNombre())){
                obj.setState("Listo");
            }
        }
        for (User obj:players.getUsers()) {
            if (obj.getNombre().equals(user.getNombre())){
                obj.setState("Listo");
            }
        }
        //this.imprimirListaUsuarios();
    }


        public List<User> cloneList() {
            List<User> clonedList = new ArrayList<>();

            for (User obj : players.getUsers()) {
                // Crear un nuevo objeto Player clonando los atributos relevantes
                User clone = new User(obj.getNombre(), obj.getClave(), obj.getState());
                //clonedPlayer.setName(player.getName()); // Ajusta esto según tu clase Player

                // Agregar el jugador clonado a la nueva lista
                clonedList.add(obj);
            }

            return clonedList;
        }

    public List<User> cloneListUsers() {
        List<User> clonedList = new ArrayList<>();

        for (User obj : users.getUsers()) {
            // Crear un nuevo objeto Player clonando los atributos relevantes
            User clone = new User(obj.getNombre(), obj.getClave(), obj.getState());
            //clonedPlayer.setName(player.getName()); // Ajusta esto según tu clase Player

            // Agregar el jugador clonado a la nueva lista
            clonedList.add(obj);
        }

        return clonedList;
    }

}