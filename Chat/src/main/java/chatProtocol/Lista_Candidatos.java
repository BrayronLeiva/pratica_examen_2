package chatProtocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lista_Candidatos implements Serializable {
    private List<Candidato> listaCandidatos;

    public Lista_Candidatos() {
        this.listaCandidatos = new ArrayList<>();
    }

    public List<Candidato> getListaCandidatos() {
        return listaCandidatos;
    }

    public void setListaCandidatos(List<Candidato> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
    }

    public void add(Candidato obj){
        this.listaCandidatos.add(obj);
    }

    public void remove(Candidato obj){
        this.listaCandidatos.remove(obj);
    }

    public void remove(int index){
        this.listaCandidatos.remove(index);
    }

    public boolean existAlready(String id){
        for (Candidato obj: this.listaCandidatos) {
            if(obj.getId().equals(id)) return true;
        }
        return false;
    }

    public Candidato get_candidato_x_id(String id){
        for (Candidato obj: this.listaCandidatos) {
            if(obj.getId().equals(id)) return obj;
        }
        return null;
    }

}
