package chatClient.presentation.View;

import chatClient.Application;
import chatClient.logic.ServiceProxy;
import chatClient.presentation.Controller.Controller;
import chatClient.presentation.Model.Model;
import chatProtocol.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class View extends JFrame {
    private JPanel panel;
    private LogIn login;
    private ListaEsperaView esperaView;
    private JButton btn_1_1;
    private JButton btn_2_1;
    private JButton btn_0_1;
    private JButton btn_0_0;
    private JButton btn_0_2;
    private JButton btn_1_0;
    private JButton btn_1_2;
    private JButton btn_2_2;
    private JButton btn_2_0;
    private JButton btnPoner;
    private JComboBox cmOption;
    private JLabel lbPlayer;
    private JComboBox cm_row;
    Model model;
    Controller controller;

    public View(Controller controller)  {
        login = new LogIn();
        esperaView = new ListaEsperaView();
        this.setContentPane(login.getLoginPanel());
        panel.setVisible(false);
        this.setSize(700,500);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle("CONECTA 4");
        try {
            this.setIconImage((new ImageIcon(Application.class.getResource("/logo.png"))).getImage());
        } catch (Exception e) {}
        this.controller = controller;
        //this.setContentPane(panel);
        this.setVisible(true);
        //this.initButtons(); //creo que por posicion y el flujo que no funciona asi

    }

    public String obtenerUsuario(){
        String user = login.getTextField1().getText();
        return user;

    }
    public String obtenerClave(){
        String password = login.getTextField2().getText();
        return password;
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

    }

    public void entrarJuego(){
        this.setContentPane(panel);
        panel.setVisible(true);
        login.getLoginPanel().setVisible(false);
    }

    public void recargarTablaEspera(List<User> listaUsuarios){
        for (User obj:listaUsuarios) {
            System.out.println(obj.getNombre() + " " + obj.getClave() + " " + obj.getState());
        }

        System.out.println("RECARGANDO TABLA----------------------------------------------------------------");
        JTable table = esperaView.getTableEspera();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        this.limpiarTabla();

        for (User usuario : listaUsuarios) {
            //System.out.println("Ingresando Usuario "+ usuario.getNombre() + " " + usuario.getClave());

            Object[] row = {usuario.getNombre(),usuario.getState()};
            model.addRow(row);
        }

    }


    public void limpiarTabla() {
        System.out.println("Limpiando tabla");
        JTable table = esperaView.getTableEspera();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
    }


    public void entrarListaEspera(){
        login.getTextField1().setText("");
        login.getTextField2().setText("");
        ServiceProxy.instance().solicitarTablaUsuarios();
        JPanel esperaP = esperaView.getPanelEspera();
        this.setContentPane(esperaP);
        this.repaint();
        this.revalidate();
        login.getLoginPanel().setVisible(false);
    }

    public JButton getBtnPoner() {
        return btnPoner;
    }
    public JComboBox getCmOption() {
        return cmOption;
    }

    public JLabel getLbPlayer() {return lbPlayer;}

    public JButton getBtn1_1() {
        return btn_1_1;
    }

    public JButton getBtn2_1() {
        return btn_2_1;
    }

    public JButton getBtn0_1() {
        return btn_0_1;
    }

    public JButton getBtn0_0() {
        return btn_0_0;
    }

    public JButton getBtn0_2() {
        return btn_0_2;
    }

    public JButton getBtn1_0() {
        return btn_1_0;
    }

    public JButton getBtn1_2() {
        return btn_1_2;
    }

    public JButton getBtn2_2() {
        return btn_2_2;
    }

    public JButton getBtn2_0() {
        return btn_2_0;
    }

    public JButton getBtnLogIn() {return login.getBtnLogIn();}
    public JButton getBtnListo() {return esperaView.getBtnListo();}

    public JButton getBtnStart() {return esperaView.getBtnStart();}

}
