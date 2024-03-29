/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient.presentation.Model;

import chatProtocol.Candidato;
import chatProtocol.Lista_Candidatos;
import chatProtocol.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model extends java.util.Observable {
    User currentUser;
    Lista_Candidatos lista_candidatos;
    List<String> messages;

    public Model() {
       currentUser = null;
       messages= new ArrayList<>();
       lista_candidatos = new Lista_Candidatos();
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
    public static int USER=1;
    public static int CHAT=2;

    public Lista_Candidatos getLista_candidatos() {
        return lista_candidatos;
    }

    public void setLista_candidatos(Lista_Candidatos lista_candidatos) {
        this.lista_candidatos = lista_candidatos;
    }

    public void imprimir_lista_candidatos(){
        System.out.println("IMPRIMIENDO CANTIDADOS DE FRONT END");
        for (Candidato obj:lista_candidatos.getListaCandidatos()) {
            System.out.println( obj.getNombre() + "\n" + obj.getId() + "\n" + obj.getVotos());
        }
    }

    public void uptade_candidato(Candidato obj){
        System.out.println("Votos de obj llegando " + obj.getVotos());
        Candidato target = lista_candidatos.get_candidato_x_id(obj.getId());
        System.out.println("Candidato a actualizar " + target.getNombre() + "\n" + target.getId() + "\n" + target.getVotos());
        target.setVotos(obj.getVotos());
        System.out.println("Candidato a actualizado " + target.getNombre() + "\n" + target.getId() + "\n" + target.getVotos());
        //this.ordenar_lista_x_votos();

    }

    public void agregar_candidato(Candidato obj){
        lista_candidatos.add(obj);
        //this.ordenar_lista_x_votos();
    }

    public void recargar_tabla(JTable table) throws Exception {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        if (table.getModel().getRowCount() > 0)
            this.limpiar_tabla(table);
        this.ordenar_lista_x_votos();
        for (int i = 0; i < this.lista_candidatos.getListaCandidatos().size(); i++){
            Candidato obj = this.lista_candidatos.getListaCandidatos().get(i);
            Object[] fila = new Object[]{obj.getId(),obj.getNombre(),obj.getVotos()};
            modelo.addRow(fila);
        }

    }

    /*1234 Pepe 5 0
    4321 Roberto 4 1
    5678 Mike 1 2
    */

    public void ordenar_lista_x_votos(){

        /*Candidato target = null;
        Lista_Candidatos list = new Lista_Candidatos();
        int tam = lista_candidatos.getListaCandidatos().size();
        for (int e = 0; e < tam;e++ ) {
            int t = lista_candidatos.getListaCandidatos().size();
            target = lista_candidatos.getListaCandidatos().get(0);
            for (int i = 0; i < t; i++) {
                Candidato obj = lista_candidatos.getListaCandidatos().get(i);
                if (obj.getVotos() > target.getVotos()) {
                    target = obj;
                }
            }
            lista_candidatos.getListaCandidatos().remove(target);
            list.add(target);
        }
        lista_candidatos = list;*/
        Collections.sort(lista_candidatos.getListaCandidatos());
    }

    public void limpiar_tabla(JTable table){
        System.out.println("Se esta limpiando la tabla");
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for (int i = 0; i < table.getRowCount(); i++){
            modelo.removeRow(i);
            i-=1;
        }

    }


}
