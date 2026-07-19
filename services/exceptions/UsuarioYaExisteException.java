package BibliotecaDigital.services.exceptions;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String mensaje) { super(mensaje); }
}
