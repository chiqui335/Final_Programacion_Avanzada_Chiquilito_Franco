public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String tipo;
    private boolean tapaDura;
    private int cantidadDisponible;
    private int cantidadTotal;
    private boolean activo;

    public Libro (int id, String titulo, String autor, String tipo, boolean tapaDura, int cantidadDisponible, int cantidadTotal, boolean activo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.tapaDura = tapaDura;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadTotal = cantidadTotal;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isTapaDura() { return tapaDura; }
    public void setTapaDura(boolean tapaDura) { this.tapaDura = tapaDura; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public int getCantidadTotal() { return cantidadTotal; }
    public void setCantidadTotal(int cantidadTotal) { this.cantidadTotal = cantidadTotal; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }


    public void prestar(){
        this.cantidadDisponible--;
    }

    public void devolver(){
        this.cantidadDisponible++;
    }

    public boolean estaDisponible(){
        return this.cantidadDisponible > 0;
    }
}