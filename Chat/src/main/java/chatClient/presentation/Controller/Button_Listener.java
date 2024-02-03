package chatClient.presentation.Controller;

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
            case "Agregar": {
                System.out.println("Se ejecuto agregar\n");
                controller.agregarCandidato();
                break;
            }
            case "Color": {

                break;
            }
            case "Votar": {
                controller.efectuarVoto();
                break;
            }
            default: break;
        }

    }

}
