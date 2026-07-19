package BibliotecaDigital.dao;

import BibliotecaDigital.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void crear(Usuario usuario);
    Usuario leer(int id);
    void actualizar(Usuario usuario);
    void eliminar(int id);
    Usuario buscarPorUsername(String username);
    List<Usuario> listarTodos();
}
