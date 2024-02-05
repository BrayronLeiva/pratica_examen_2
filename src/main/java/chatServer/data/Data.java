package chatServer.data;

import chatProtocol.Candidato;
import chatProtocol.Lista_Candidatos;
import chatProtocol.Position;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<User> users;

    // Declarar una matriz de enteros con tamaño 3x3
    private Position[][] tablero;


    public Data() {
        users = new ArrayList<>();
        tablero = new Position[6][7];
        this.inicializarTablero();
        this.imprimirTablero();

    }

    public void inicializarTablero(){
        for (int i = 0; i<7; i++){
            for (int e = 0; e<6; e++){
                //System.out.println("Se agrego en" + i + e + "\t");
                tablero[e][i] = new Position(e, i,"vacio");
            }
            //System.out.println("Salto\n");
        }

    }

    public void imprimirTablero(){
        for (int i = 0; i<6; i++){
            for (int e = 0; e<7; e++){
                System.out.print(tablero[i][e].getState() +tablero[i][e].getNumW() + " ");
            }
            System.out.println("\n");
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
        for (int i = 0; i<6; i++){
            if(tablero[0][colum].getState().equals("gamed")){
                System.out.println("Ya no hay espacio en esa columna");
                return false;
            }
            if(tablero[5][colum].getState().equals("vacio")){
                tablero[5][colum] = obj;
                return true;
            }
            if (tablero[i][colum].getState()=="vacio"){
            }else{
                tablero[i-1][colum] = obj;
                return true;
            }
        }

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

        for (int i = 0; i<6; i++){
            for (int e = 0; e<7; e++){
                if(tablero[i][e].getState().equals("gamed") && tablero[i][e].getNumW()==1){
                    player1++;
                    player2 = 0;
                } else if (tablero[i][e].getState().equals("gamed") && tablero[i][e].getNumW()==2) {
                    player2++;
                    player1 = 0;
                }
                if (player1==4){
                    return "Player 1 Gano\n";
                } else if (player2==4) {
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

        for (int i = 0; i<7; i++){
            for (int e = 0; e<6; e++){
                if(tablero[e][i].getState().equals("gamed") && tablero[e][i].getNumW()==1){
                    player1++;
                    player2 = 0;
                } else if (tablero[e][i].getState().equals("gamed") && tablero[e][i].getNumW()==2) {
                    player2++;
                    player1 = 0;
                }
                if (player1==4){
                    return "Player 1 Gano\n";
                } else if (player2==4) {
                    return "Plater 2 Gano\n";
                }
            }
            player1 = 0;
            player2 = 0;
        }
        return "";
    }



}
