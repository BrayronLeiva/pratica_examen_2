package chatClient.presentation.View;

import javax.swing.*;

public class LogIn {
    private JPanel loginPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton btnLogIn;

    public LogIn(){
        //this.setContentPane(loginPanel);
        loginPanel.setVisible(true);
        //this.setSize(700,500);
        //this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //this.setTitle("3 EN RAYA");

        //this.setVisible(true);
        //this.initButtons(); //creo que por posicion y el flujo que no funciona asi
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public JButton getBtnLogIn() {return btnLogIn;}
}
