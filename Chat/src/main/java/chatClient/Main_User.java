package chatClient;

import chatClient.presentation.Controller;
import chatClient.presentation.Model;
import chatClient.presentation.View;

import javax.swing.*;


public class Main_User {
    public static JFrame window;
    Main_User() throws Exception {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ex) {};
        window = new JFrame();
        Model model= new Model();
        View view = new View(window);
        Controller controller =new Controller(view, model);

    }




}
