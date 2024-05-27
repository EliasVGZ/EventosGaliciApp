package modelos;

public class Conciertos {

    private int id;
    private String nombreConciertos;
    private String fecha;
    private String lugar;
    private String ciudad;
    private String genero;
    private String precio;
    private int imagen;
    private String compraEntrada;
    private String imagenUrl;
    private String nombreId;

    public Conciertos(int id, String nombreConciertos, String fecha, String lugar, String ciudad, String genero, String precio, int imagen) {
        this.id = id;
        this.nombreConciertos = nombreConciertos;
        this.fecha = fecha;
        this.lugar = lugar;
        this.ciudad = ciudad;
        this.genero = genero;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Conciertos(int id, String nombreConciertos, String fecha, String lugar, String ciudad, String genero, String precio, int imagen, String compraEntrada) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreId() {
        return nombreId;
    }

    public void setNombreId(String nombreId) {
        this.nombreId = nombreId;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getCompraEntrada() {
        return compraEntrada;
    }

    public void setCompraEntrada(String compraEntrada) {
        this.compraEntrada = compraEntrada;
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
