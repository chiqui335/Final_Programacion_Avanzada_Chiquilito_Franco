package BibliotecaDigital;

import BibliotecaDigital.dao.LibroDAO;
import BibliotecaDigital.dao.PrestamoDAO;
import BibliotecaDigital.dao.UsuarioDAO;
import BibliotecaDigital.dao.impl.LibroDAOImpl;
import BibliotecaDigital.dao.impl.PrestamoDAOImpl;
import BibliotecaDigital.dao.impl.UsuarioDAOImpl;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        LibroDAO libroDAO = new LibroDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        PrestamoDAO prestamoDAO = new PrestamoDAOImpl();

        BibliotecaServices servicios = new BibliotecaServices(libroDAO, usuarioDAO, prestamoDAO);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(servicios);
            ventana.setVisible(true);
        });
    }
}
