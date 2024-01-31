package chatProtocol;

import java.io.Serializable;

public class Candidato implements Serializable {
    private String id;
    private String nombre;
    private int votos;

    public Candidato() {
    }
    public Candidato(String id, String nombre, int votos) {
        this.id = id;
        this.nombre = nombre;
        this.votos = votos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public void agregarVoto(){votos = votos+1;}

    @Override
    public String toString() {
        return "Candidato{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", votos=" + votos +
                '}';
    }
}
