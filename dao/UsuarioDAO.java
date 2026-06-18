public interface UsuarioDAO {
    void crear(Usuario usuario);
    Usuario leer(int id);
    void actualizar(Usuario usuario);
    void eliminar(int id);
    Usuario buscarPorUsernames(String username);

    List<Usuario> listarTodos();
}