package BibliotecaDigital.dao.impl;

import BibliotecaDigital.dao.UsuarioDAO;
import BibliotecaDigital.model.Admin;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.model.UsuarioRegular;
import BibliotecaDigital.services.exceptions.PersistenciaException;
import BibliotecaDigital.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    public void crear(Usuario usuario) {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "INSERT INTO usuario (nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado, activo, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getUsername());
                stmt.setString(3, usuario.getPassword());
                stmt.setString(4, usuario.getDni());
                stmt.setString(5, usuario.getEmail());
                stmt.setInt(6, usuario.getPrestamosActuales());
                stmt.setInt(7, usuario.getTotalPrestamos());
                stmt.setBoolean(8, usuario.isRetrasado());
                stmt.setBoolean(9, usuario.isActivo());
                stmt.setString(10, usuario.getRol());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear el usuario", e);
        }
    }

    public Usuario leer(int id) {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "SELECT * FROM usuario WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        String dni = rs.getString("dni");
                        String email = rs.getString("email");
                        int prestamosActuales = rs.getInt("prestamosActuales");
                        int totalPrestamos = rs.getInt("totalPrestamos");
                        boolean estaRetrasado = rs.getBoolean("estaRetrasado");
                        String rol = rs.getString("rol");
                        if (rol.equals("ADMIN")) {
                            return new Admin(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado);
                        } else {
                            return new UsuarioRegular(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al leer el usuario con id " + id, e);
        }
        return null;
    }

    public void actualizar(Usuario usuario) {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "UPDATE usuario SET nombre=?, username=?, password=?, dni=?, email=?, prestamosActuales=?, totalPrestamos=?, estaRetrasado=?, activo=?, rol=? WHERE id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getUsername());
                stmt.setString(3, usuario.getPassword());
                stmt.setString(4, usuario.getDni());
                stmt.setString(5, usuario.getEmail());
                stmt.setInt(6, usuario.getPrestamosActuales());
                stmt.setInt(7, usuario.getTotalPrestamos());
                stmt.setBoolean(8, usuario.isRetrasado());
                stmt.setBoolean(9, usuario.isActivo());
                stmt.setString(10, usuario.getRol());
                stmt.setInt(11, usuario.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el usuario con id " + usuario.getId(), e);
        }
    }

    public void eliminar(int id) {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "UPDATE usuario SET activo = false WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el usuario con id " + id, e);
        }
    }

    public Usuario buscarPorUsername(String username) {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "SELECT * FROM usuario WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String usernameDB = rs.getString("username");
                        String password = rs.getString("password");
                        String dni = rs.getString("dni");
                        String email = rs.getString("email");
                        int prestamosActuales = rs.getInt("prestamosActuales");
                        int totalPrestamos = rs.getInt("totalPrestamos");
                        boolean estaRetrasado = rs.getBoolean("estaRetrasado");
                        String rol = rs.getString("rol");
                        if (rol.equals("ADMIN")) {
                            return new Admin(id, nombre, usernameDB, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado);
                        } else {
                            return new UsuarioRegular(id, nombre, usernameDB, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el usuario con username " + username, e);
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        try {
            Connection connection = ConexionDB.getInstance().getConnection();
            String sql = "SELECT * FROM usuario WHERE activo = true";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                List<Usuario> usuarios = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String dni = rs.getString("dni");
                    String email = rs.getString("email");
                    int prestamosActuales = rs.getInt("prestamosActuales");
                    int totalPrestamos = rs.getInt("totalPrestamos");
                    boolean estaRetrasado = rs.getBoolean("estaRetrasado");
                    String rol = rs.getString("rol");
                    if (rol.equals("ADMIN")) {
                        usuarios.add(new Admin(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado));
                    } else {
                        usuarios.add(new UsuarioRegular(id, nombre, username, password, dni, email, prestamosActuales, totalPrestamos, estaRetrasado));
                    }
                }
                return usuarios;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los usuarios", e);
        }
    }
}
