package modelos;

public class Conciertos {

    private int id_concierto;
    private String nombreConciertos;
    private String fecha;
    private String lugar;
    private String ciudad;
    private String genero;
    private String precio;
    private int imagen;
    private String compraEntrada;

    public Conciertos(int id_concierto, String nombreConciertos, String fecha, String lugar, String ciudad, String genero, String precio, int imagen) {
        this.id_concierto = id_concierto;
        this.nombreConciertos = nombreConciertos;
        this.fecha = fecha;
        this.lugar = lugar;
        this.ciudad = ciudad;
        this.genero = genero;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Conciertos(int id_concierto, String nombreConciertos, String fecha, String lugar, String ciudad, String genero, String precio, int imagen, String compraEntrada) {
        this.id_concierto = id_concierto;
        this.nombreConciertos = nombreConciertos;
        this.fecha = fecha;
        this.lugar = lugar;
        this.ciudad = ciudad;
        this.genero = genero;
        this.precio = precio;
        this.imagen = imagen;
        this.compraEntrada = compraEntrada;
    }

    public Conciertos() {
    }

    public String getCompraEntrada() {
        return compraEntrada;
    }

    public void setCompraEntrada(String compraEntrada) {
        this.compraEntrada = compraEntrada;
    }

    public int getId_concierto() {
        return id_concierto;
    }

    public void setId_concierto(int id_concierto) {
        this.id_concierto = id_concierto;
    }

    public String getNombreConciertos() {
        return nombreConciertos;
    }

    public void setNombreConciertos(String nombreConciertos) {
        this.nombreConciertos = nombreConciertos;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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
