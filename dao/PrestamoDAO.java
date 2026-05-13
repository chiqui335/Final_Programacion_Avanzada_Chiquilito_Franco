public interface PrestamoDAO {
    void crear(Prestamo prestamo);
    Prestamo leer(int id);
    void actualizar(Prestamo prestamo);
    void eliminar(int id);
    List<Prestamo> listarActivos();
}