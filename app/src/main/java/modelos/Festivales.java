package modelos;

public class Festivales {
    private int id_festival;
    private String nombre_festival;
    private String fecha_inicio;
    private String fecha_fin;
    private String ubicacion;

    public Festivales(int id_festival, String nombre_festival, String fecha_inicio, String fecha_fin, String ubicacion) {
        this.id_festival = id_festival;
        this.nombre_festival = nombre_festival;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.ubicacion = ubicacion;
    }

    public Festivales() {
    }

    public int getId_festival() {
        return id_festival;
    }

    public void setId_festival(int id_festival) {
        this.id_festival = id_festival;
    }

    public String getNombre_festival() {
        return nombre_festival;
    }

    public void setNombre_festival(String nombre_festival) {
        this.nombre_festival = nombre_festival;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
