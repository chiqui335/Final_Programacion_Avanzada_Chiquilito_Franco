package BibliotecaDigital.services.exceptions;

public class LibroYaExisteException extends RuntimeException {
    public LibroYaExisteException(String mensaje) { super(mensaje); }
}
