package BibliotecaDigital.services;

import java.util.List;
import BibliotecaDigital.dao.LibroDAO;

public class BibliotecaServices {

    private LibroDAO libroDAO;

    public BibliotecaServices() {
        this.libroDAO = new LibroDAOImpl();
    }

    public List<Libro> listarTodosLosLibros() {
        return libroDAO.listarTodos();
    }
}
