/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient.presentation.Model;

import chatProtocol.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model extends java.util.Observable {
    User currentUser;
    List<String> messages;

    public Model() {
       currentUser = null;
       messages= new ArrayList<>();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        this.commit(Model.USER+Model.CHAT);
    }
    
    public void commit(int properties){
        this.setChanged();
        this.notifyObservers(properties);        
    }

    public void limpiar_tabla(JTable table){
        System.out.println("Se esta limpiando la tabla");
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for (int i = 0; i < table.getRowCount(); i++){
            modelo.removeRow(i);
            i-=1;
        }

    }

    public static int USER=1;
    public static int CHAT=2;


}
