package BibliotecaDigital.dao;

import BibliotecaDigital.model.Libro;
import java.util.List;

public interface LibroDAO {
    void crear(Libro libro);
    Libro leer(int id);
    void actualizar(Libro libro);
    void eliminar(int id);
    List<Libro> buscarPorTitulo(String titulo);
    List<Libro> listarTodos();
}
