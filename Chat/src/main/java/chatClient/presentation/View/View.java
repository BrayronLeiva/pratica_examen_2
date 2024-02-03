package chatClient.presentation.View;

import chatClient.Application;
import chatClient.presentation.Controller.Controller;
import chatClient.presentation.Model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private JPanel panel;
    private JTable table_candidatos;
    private JButton btn_votar;
    private JTextField txf_id;
    private JTextField txf_nombre;
    private JButton btn_color;
    private JButton btn_agregar;
    private JLabel lb_nombre;
    private JLabel lb_id;
    private JFrame window;
    Model model;
    Controller controller;

    public View(Controller controller) {
        panel.setVisible(true);
        //Application.window.getRootPane().setDefaultButton(login);
        //bodyPanel.setVisible(false);

        //DefaultCaret caret = (DefaultCaret) messages.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.window = new JFrame();
        window.setSize(700,500);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.setTitle("VOTACION");
        try {
            window.setIconImage((new ImageIcon(Application.class.getResource("/logo.png"))).getImage());
        } catch (Exception e) {}
        this.controller = controller;
        window.setContentPane(panel);
        window.setVisible(true);
        this.initTable();
        //this.initButtons(); //creo que por posicion y el flujo que no funciona asi

    }

    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
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

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Se ejecuto el metodo uptade en la vista");
    }

    public void lanzar_mensaje(String msg){
        JOptionPane.showMessageDialog(window, msg, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiar_interfaz(){
        txf_id.setText("");
        txf_nombre.setText("");
        table_candidatos.clearSelection();
    }

    private void initTable(){
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Numero");
        modelo.addColumn("Nombre");
        modelo.addColumn("Votos");

        table_candidatos.setModel(modelo);
        table_candidatos.setVisible(true);
    }

    public void initButtons(){
        btn_agregar.addActionListener(controller.getButton_listener());

        btn_votar.addActionListener(controller.getButton_listener());
    }


/*
    public void update(java.util.Observable updatedModel, Object properties) {

        int prop = (int) properties;
        if (model.getCurrentUser() == null) {
            Application.window.setTitle("CHAT");
            //loginPanel.setVisible(true);
            //Application.window.getRootPane().setDefaultButton(login);
            //bodyPanel.setVisible(false);
        } else {
            Application.window.setTitle(model.getCurrentUser().getNombre().toUpperCase());
            //loginPanel.setVisible(false);
            //bodyPanel.setVisible(true);
            //Application.window.getRootPane().setDefaultButton(post);
            if ((prop & Model.CHAT) == Model.CHAT) {
                //this.messages.setText("");
                String text = "";
                for (Message m : model.getMessages()) {
                    if (m.getSender().equals(model.getCurrentUser())) {
                        text += ("Me:" + m.getMessage() + "\n");
                    } else {
                        text += (m.getSender().getNombre() + ": " + m.getMessage() + "\n");
                     }
                }
                //this.messages.setText(text);
            }
        }
        panel.validate();
    }*/

    public JTable getTable_candidatos() {
        return table_candidatos;
    }

    public JButton getBtn_votar() {
        return btn_votar;
    }

    public JTextField getTxf_id() {
        return txf_id;
    }

    public JTextField getTxf_nombre() {
        return txf_nombre;
    }

    public JButton getBtn_color() {
        return btn_color;
    }

    public JButton getBtn_agregar() {
        return btn_agregar;
    }

    public JFrame getWindow() { return window;  }
}
