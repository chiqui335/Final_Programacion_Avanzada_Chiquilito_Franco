package BibliotecaDigital.model;

import java.time.LocalDate;

public class Admin extends Usuario {
    public Admin(int id, String nombre, String username, String password, String dni, String email, int prestamosActuales, int totalPrestamos, boolean estaRetrasado) {
        super(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado, true, "ADMIN");
    }

    @Override
    public Prestamo generarPrestamo(Libro libro) {
        return new Prestamo(0, LocalDate.now(), LocalDate.now().plusDays(30), false, false, this, libro);
    }

    public Prestamo generarPrestamoANombre(Usuario usuario, Libro libro) {
        return new Prestamo(0, LocalDate.now(), LocalDate.now().plusDays(30), false, false, usuario, libro);
    }
}
