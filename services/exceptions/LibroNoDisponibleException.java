package BibliotecaDigital.services.exceptions;

public class LibroNoDisponibleException extends RuntimeException {
    public LibroNoDisponibleException(String mensaje) { super(mensaje); }
}
