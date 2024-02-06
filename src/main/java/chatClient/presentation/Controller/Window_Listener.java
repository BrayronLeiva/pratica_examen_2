package chatClient.presentation.Controller;

import chatClient.presentation.Controller.Controller;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window_Listener implements WindowListener {
    private Controller controller;
    public Window_Listener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            JFrame frame = controller.getView();
            JOptionPane.showMessageDialog(frame, "Cierra Con Botones", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
