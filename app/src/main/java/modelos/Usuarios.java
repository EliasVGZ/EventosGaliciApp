package modelos;

public class Usuarios {

    private String nombreUsuario;
    private String contrasena;

public Usuarios(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }
    public Usuarios() {
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void verEvento(Evento evento) {
        // Implementación para ver evento
    }

    public void comprarEntrada(Evento evento) {
        // Implementación para comprar entrada
    }

    public void opinar(Evento evento, String opinion) {
        // Implementación para opinar
    }
}
