package chatClient.presentation.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ListaEsperaView {
    private JLabel lbEspera;
    private JTable tableEspera;
    private JPanel panelEspera;
    private JButton btnListo;
    private JTextField textField1;
    private JButton btnStart;

    public ListaEsperaView() {
        panelEspera.setVisible(true);
        initTable();
        btnStart.setEnabled(false);
        textField1.setEnabled(false);
    }

    public JTable getTableEspera() {
        return tableEspera;
    }

    public JPanel getPanelEspera() {
        return panelEspera;
    }

    public JButton getBtnListo() {return btnListo;}

    public JButton getBtnStart() {return btnStart;}

    public void initTable(){
        // Inicializar el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel();

        // Agregar la columna "Usuario" al modelo
        model.addColumn("Usuario");
        model.addColumn("Espera");
        tableEspera.setModel(model);
    }
}