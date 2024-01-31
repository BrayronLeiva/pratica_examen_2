package chatClient.presentation;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class Window_Listener implements WindowListener {
    private Controller controller;
    public Window_Listener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            controller.logout();
            JFrame frame = controller.getView().getWindow();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispose();

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
