package BibliotecaDigital.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected String username;
    protected String password;
    protected String dni;
    protected String email;
    protected int prestamosActuales;
    protected int totalPrestamos;
    protected boolean estaRetrasado;
    protected boolean activo;
    protected String rol;
    protected List<Prestamo> prestamos;

    public Usuario(int id, String nombre, String username, String password, String dni, String email, int prestamosActuales, int totalPrestamos, boolean estaRetrasado, boolean activo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.dni = dni;
        this.email = email;
        this.prestamosActuales = prestamosActuales;
        this.totalPrestamos = totalPrestamos;
        this.estaRetrasado = estaRetrasado;
        this.activo = activo;
        this.rol = rol;
        this.prestamos = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPrestamosActuales() { return prestamosActuales; }
    public void setPrestamosActuales(int prestamosActuales) { this.prestamosActuales = prestamosActuales; }

    public int getTotalPrestamos() { return totalPrestamos; }
    public void setTotalPrestamos(int totalPrestamos) { this.totalPrestamos = totalPrestamos; }

    public boolean isRetrasado() { return estaRetrasado; }
    public void setEstaRetrasado(boolean estaRetrasado) { this.estaRetrasado = estaRetrasado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<Prestamo> getPrestamos() { return prestamos; }

    public abstract Prestamo generarPrestamo(Libro libro);
    public void modificarPerfil() {}
}
