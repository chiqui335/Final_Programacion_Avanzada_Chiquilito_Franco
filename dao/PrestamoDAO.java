package BibliotecaDigital.dao;

import BibliotecaDigital.model.Prestamo;
import java.util.List;

public interface PrestamoDAO {
    void crear(Prestamo prestamo);
    Prestamo leer(int id);
    void actualizar(Prestamo prestamo);
    void eliminar(int id);
    List<Prestamo> listarActivos();
    List<Prestamo> listarActivosPorUsuario(int usuarioId);
    List<Integer> librosMasSolicitados();
    List<Integer> usuariosConMasPrestamos();
}
