public class UsuarioRegular extends Usuario {
    public UsuarioRegular (int id, String nombre, String username, String password, String dni, String email, int prestamosActuales, int totalPrestamos, boolean estaRetrasado) {
        super(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado, true, "REGULAR");    
        }

    
    //metodos
    @Override
    public Prestamo generarPrestamo(Libro libro){
        return new Prestamo(0, LocalDate.now(), LocalDate.now().plusDays(30), false, false, this, libro);
    }

}