package chatClient.presentation.Controller;

import chatClient.logic.ServiceProxy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button_Listener implements ActionListener {

    Controller controller;
    public Button_Listener(Controller ctl) {
        controller = ctl;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "btn_0_0": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_0_0");
                break;
            }
            case "btn_1_0": {
                System.out.println("Se ejecuto 1 0\n");
                controller.ponerFicha("btn_1_0");
                break;
            }
            case "btn_2_0": {
                System.out.println("Se ejecuto 2 0\n");
                controller.ponerFicha("btn_2_0");
                break;
            }
            case "btn_0_1": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_0_1");
                break;
            }
            case "btn_1_1": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_1_1");
                break;
            }
            case "btn_2_1": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha( "btn_2_1");
                break;
            }
            case "btn_0_2": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_0_2");
                break;
            }
            case "btn_1_2": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_1_2");
                break;
            }
            case "btn_2_2": {
                System.out.println("Se ejecuto poner\n");
                controller.ponerFicha("btn_2_2");
                break;
            }
            case "Log In": {
                System.out.println("Se ejecuto login\n");
                controller.read();

                break;
            }
            case "Listo": {
                System.out.println("Se ejecuto listo\n");
                controller.ready();

                break;
            }
            case "Comenzar": {
                System.out.println("Se ejecuto comenzar\n");
                controller.entrarJuego();

                break;
            }
            case "Salir": {
                System.out.println("Se ejecuto salir\n");
                controller.salirJuego();

                break;
            }
            case "Cerrar Sesión": {
                System.out.println("Se ejecuto cerrar secion\n");
                controller.logout();
                //ServiceProxy.instance().solicitarTablaUsuarios();
                JFrame frame = controller.getView();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.dispose();

                break;
            }
            default: break;
        }

    }

}
