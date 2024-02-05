package chatProtocol;

public class Protocol {

    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    public static final int LOGIN=1;
    public static final int LOGOUT=2;    
    public static final int POST=3;

    public static final int DELIVER=10;
    
    public static final int ERROR_NO_ERROR=0;
    public static final int ERROR_LOGIN=1;
    public static final int ERROR_LOGOUT=2;    
    public static final int ERROR_POST=3;
    public static final int ADD_CANTIDATO= 100;
    public static final int ADD_CANTIDATO_LISTA=200;
    public static final int SEND_LISTA_CANTIDADOS=300;
    public static final int INIT_LISTA_CANTIDATOS=300;
    public static final int VOTO_EFECTUADO= 400;
    public static final int UPTADE_CANTIDATO_LISTA=500;

    //----------------------------------New Game

    public static final int ENVIAR_FICHA = 1000;
    public static final int REQUEST_NUMERO_WORKER = 1001;
    public static final int SEND_NUMERO_WORKER = 1002;
    public static final int SEND_PLAYER_PLAYED = 1003;
}