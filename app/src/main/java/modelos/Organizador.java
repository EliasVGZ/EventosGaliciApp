package modelos;

public class Organizador {
    private String nombre;
    private String contrasenha;

    // Constructor
    public Organizador(String nombre) {
        this.nombre = nombre;
    }

    public Organizador(String nombre, String contrasenha) {
        this.nombre = nombre;
        this.contrasenha = contrasenha;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    // Métodos
    public void crearEvento(Evento evento) {
        // Implementación para crear evento
    }
}

