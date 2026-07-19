package BibliotecaDigital.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    private int id;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private boolean finalizado;
    private boolean retrasado;
    private Usuario usuario;
    private Libro libro;

    public Prestamo(int id, LocalDate fechaInicio, LocalDate fechaDevolucion, boolean finalizado, boolean retrasado, Usuario usuario, Libro libro) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = fechaDevolucion;
        this.finalizado = finalizado;
        this.retrasado = retrasado;
        this.usuario = usuario;
        this.libro = libro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }

    public boolean isRetrasado() { return retrasado; }
    public void setRetrasado(boolean retrasado) { this.retrasado = retrasado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    public long getDiasRestantes() { return ChronoUnit.DAYS.between(LocalDate.now(), fechaDevolucion); }
    public void finalizar() { this.finalizado = true; }
    public boolean verificarRetraso() { return LocalDate.now().isAfter(fechaDevolucion); }
}
