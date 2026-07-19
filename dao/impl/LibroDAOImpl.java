package BibliotecaDigital.dao.impl;

import BibliotecaDigital.dao.LibroDAO;
import BibliotecaDigital.model.Libro;
import BibliotecaDigital.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    public void crear(Libro libro) {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "INSERT INTO libro (titulo, autor, tipo, tapaDura, activo, cantidadDisponible, cantidadTotal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getTipo());
            stmt.setBoolean(4, libro.isTapaDura());
            stmt.setBoolean(5, libro.isActivo());
            stmt.setInt(6, libro.getCantidadDisponible());
            stmt.setInt(7, libro.getCantidadTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Libro leer(int id) {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "SELECT * FROM libro WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                boolean tapaDura = rs.getBoolean("tapaDura");
                boolean activo = rs.getBoolean("activo");
                int cantidadDisponible = rs.getInt("cantidadDisponible");
                int cantidadTotal = rs.getInt("cantidadTotal");
                return new Libro(id, titulo, autor, tipo, tapaDura, cantidadDisponible, cantidadTotal, activo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizar(Libro libro) {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "UPDATE libro SET titulo = ?, autor = ?, tipo = ?, tapaDura = ?, activo = ?, cantidadDisponible = ?, cantidadTotal = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getTipo());
            stmt.setBoolean(4, libro.isTapaDura());
            stmt.setBoolean(5, libro.isActivo());
            stmt.setInt(6, libro.getCantidadDisponible());
            stmt.setInt(7, libro.getCantidadTotal());
            stmt.setInt(8, libro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "UPDATE libro SET activo = false WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "SELECT * FROM libro WHERE titulo LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            List<Libro> libros = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String tituloLibro = rs.getString("titulo");
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                boolean tapaDura = rs.getBoolean("tapaDura");
                boolean activo = rs.getBoolean("activo");
                int cantidadDisponible = rs.getInt("cantidadDisponible");
                int cantidadTotal = rs.getInt("cantidadTotal");
                libros.add(new Libro(id, tituloLibro, autor, tipo, tapaDura, cantidadDisponible, cantidadTotal, activo));
            }
            return libros;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Libro> listarTodos() {
        try (Connection connection = ConexionDB.getInstance().getConnection()) {
            String sql = "SELECT * FROM libro WHERE activo = true";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Libro> libros = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                boolean tapaDura = rs.getBoolean("tapaDura");
                boolean activo = rs.getBoolean("activo");
                int cantidadDisponible = rs.getInt("cantidadDisponible");
                int cantidadTotal = rs.getInt("cantidadTotal");
                libros.add(new Libro(id, titulo, autor, tipo, tapaDura, cantidadDisponible, cantidadTotal, activo));
            }
            return libros;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
