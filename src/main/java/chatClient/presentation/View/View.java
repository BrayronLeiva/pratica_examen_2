package chatClient.presentation.View;

import chatClient.Application;
import chatClient.presentation.Controller.Controller;
import chatClient.presentation.Model.Model;

import javax.swing.*;

public class View extends JFrame {
    private JPanel panel;
    private JButton btnPoner;
    private JButton btnA1;
    private JButton btnA2;
    private JButton btnA3;
    private JButton btnA4;
    private JButton btnA5;
    private JButton btnA6;
    private JButton btnB1;
    private JButton btnB2;
    private JButton btnB3;
    private JButton btnB4;
    private JButton btnB5;
    private JButton btnB6;
    private JButton btnC1;
    private JButton btnC2;
    private JButton btnC3;
    private JButton btnC4;
    private JButton btnC5;
    private JButton btnC6;
    private JButton btnD1;
    private JButton btnD2;
    private JButton btnD3;
    private JButton btnD4;
    private JButton btnD5;
    private JButton btnD6;
    private JButton btnE1;
    private JButton btnE2;
    private JButton btnE3;
    private JButton btnE4;
    private JButton btnE5;
    private JButton btnE6;
    private JButton btnF1;
    private JButton btnF2;
    private JButton btnF3;
    private JButton btnF4;
    private JButton btnF5;
    private JButton btnG6;
    private JButton btnG1;
    private JButton btnG2;
    private JButton btnG3;
    private JButton btnG4;
    private JButton btnG5;
    private JButton btnF6;
    private JComboBox cmOption;
    private JLabel lbA;
    private JLabel lbB;
    private JLabel lb_C;
    private JLabel lb_D;
    private JLabel lb_E;
    private JLabel lb_F;
    private JLabel lb_G;
    private JLabel lbPlayer;
    private JComboBox cm_row;
    Model model;
    Controller controller;

    public View(Controller controller)  {
        panel.setVisible(true);
        this.setSize(700,500);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle("CONECTA 4");
        try {
            this.setIconImage((new ImageIcon(Application.class.getResource("/logo.png"))).getImage());
        } catch (Exception e) {}
        this.controller = controller;
        this.setContentPane(panel);
        this.setVisible(true);
        //this.initButtons(); //creo que por posicion y el flujo que no funciona asi

    }

    public void setModel(Model model) {
        this.model = model;
        //model.addObserver(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public JPanel getPanel() {
        return panel;
    }

    String backStyle = "margin:0px; background-color:#e6e6e6;";
    String senderStyle = "background-color:#c2f0c2;margin-left:30px; margin-right:5px;margin-top:3px; padding:2px; border-radius: 25px;";
    String receiverStyle = "background-color:white; margin-left:5px; margin-right:30px; margin-top:3px; padding:2px;";


    public void lanzar_mensaje(String msg){
        JOptionPane.showMessageDialog(this, msg, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiar_interfaz(){
        cmOption.setSelectedItem("");
    }

    public JButton getBtnPoner() {
        return btnPoner;
    }
    public JComboBox getCmOption() {
        return cmOption;
    }

    public JLabel getLbPlayer() {return lbPlayer;}
}
