package modelos;

public class FiestasPopulares {


    private int id_festa;
    private String nombreFesta;
    private String fecha;
    private String lugar;
    private String ciudad;
    private String precio;
    private int imagen;


    public FiestasPopulares(int id_festa, String nombreFesta, String fecha, String lugar, String ciudad, String precio, int imagen) {
        this.id_festa = id_festa;
        this.nombreFesta = nombreFesta;
        this.fecha = fecha;
        this.lugar = lugar;
        this.ciudad = ciudad;
        this.precio = precio;
        this.imagen = imagen;
    }
    public FiestasPopulares() {
    }

    public int getId_festa() {
        return id_festa;
    }

    public void setId_festa(int id_festa) {
        this.id_festa = id_festa;
    }

    public String getNombreFesta() {
        return nombreFesta;
    }

    public void setNombreFesta(String nombreFesta) {
        this.nombreFesta = nombreFesta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
