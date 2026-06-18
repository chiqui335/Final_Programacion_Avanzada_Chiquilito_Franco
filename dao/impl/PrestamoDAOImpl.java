package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import dao.PrestamoDAO;
import model.Prestamo;
import util.ConexionDB;

public class PrestamoDAOImpl implements PrestamoDAO{

    public void crear(Prestamo prestamo) {
        try(Connection connection = ConexionDB.getInstance().getConnection();){

            String sql = "INSERT INTO prestamo (fechaInicio, fechaDevolucion, finalizado, retrasado, usuario_id, libro_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, prestamo.getFechaInicio());
            stmt.setObject(2, prestamo.getFechaDevolucion());
            stmt.setBoolean(3, prestamo.isFinalizado());
            stmt.setBoolean(4, prestamo.isRetrasado());
            stmt.setInt(5, prestamo.getUsuario().getId());
            stmt.setInt(6, prestamo.getLibro().getId());



            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Prestamo leer(int id) {
        try (Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "SELECT * FROM prestamo WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt (1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                LocalDate fechaInicio = rs.getObject("fechaInicio", LocalDate.class);
                LocalDate fechaDevolucion = rs.getObject("fechaDevolucion", LocalDate.class);
                boolean finalizado = rs.getBoolean("finalizado");
                boolean retrasado = rs.getBoolean("retrasado");

                int usuarioId = rs.getInt("usuario_id");
                int libroId = rs.getInt("libro_id");

                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                LibroDAO libroDAO = new LibroDAOImpl();

                Usuario usuario = usuarioDAO.leer(usuarioId);
                Libro libro = libroDAO.leer(libroId);

                return new Prestamo(id, fechaInicio, fechaDevolucion, finalizado, retrasado, usuario, libro);

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void actualizar(Prestamo prestamo) {
        try(Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "UPDATE prestamo SET fechaInicio = ?, fechadevolucion = ?, finalizado = ?, retrasado = ?, usuario_id = ?, libro_id = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setObject(1, prestamo.getFechaInicio());
            stmt.setObject(2, prestamo.getFechaDevolucion());
            stmt.setBoolean(3, prestamo.isFinalizado());
            stmt.setBoolean(4, prestamo.isRetrasado());
            stmt.setInt(5, prestamo.getUsuario().getId());
            stmt.setInt(6, prestamo.getLibro().getId());
            stmt.setInt(7, prestamo.getId());


            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void eliminar(int id) {
        try (Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "UPDATE prestamo SET finalizado = true WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Prestamo> listarActivos() {

        try (Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "SELECT * FROM prestamo WHERE finalizado = false";
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            List<Prestamo> prestamos = new ArrayList<>();
            while(rs.next()) {

                int id = rs.getInt("id");
                LocalDate fechaInicio = rs.getObject("fechaInicio", LocalDate.class);
                LocalDate fechaDevolucion = rs.getObject("fechaDevolucion", LocalDate.class);
                boolean finalizado = rs.getBoolean("finalizado");
                boolean retrasado = rs.getBoolean("retrasado");

                int usuarioId = rs.getInt("usuario_id");
                int libroId = rs.getInt("libro_id");

                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                LibroDAO libroDAO = new LibroDAOImpl();

                Usuario usuario = usuarioDAO.leer(usuarioId);
                Libro libro = libroDAO.leer(libroId);

                prestamos.add(new Prestamo(id, fechaInicio, fechaDevolucion, finalizado, retrasado, usuario, libro));
            }
            return prestamos;


        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }


    public List<Prestamo> listarActivosPorUsuario(int usuarioId){

        try (Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "SELECT * FROM prestamo WHERE finalizado = false AND usuario_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            List<Prestamo> prestamos = new ArrayList<>();
            while(rs.next()) {

                int id = rs.getInt("id");
                LocalDate fechaInicio = rs.getObject("fechaInicio", LocalDate.class);
                LocalDate fechaDevolucion = rs.getObject("fechaDevolucion", LocalDate.class);
                boolean finalizado = rs.getBoolean("finalizado");
                boolean retrasado = rs.getBoolean("retrasado");

                int usuarioIdDB = rs.getInt("usuario_id");
                int libroId = rs.getInt("libro_id");

                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                LibroDAO libroDAO = new LibroDAOImpl();

                Usuario usuario = usuarioDAO.leer(usuarioId);
                Libro libro = libroDAO.leer(libroId);

                prestamos.add(new Prestamo(id, fechaInicio, fechaDevolucion, finalizado, retrasado, usuario, libro));
            }
            return prestamos;


        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Integer> librosMasSolicitados(){
        try(Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "SELECT libro_id, COUNT(*) as total FROM prestamo GROUP BY libro_id ORDER BY total DESC LIMIT 3";
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            List<Integer> ids = new ArrayList<>();

            while(rs.next()) {
                int libroId = rs.getInt("libro_id");
                ids.add(libroId);
            }
            return ids;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> usuariosConMasPrestamos(){
        try(Connection connection = ConexionDB.getInstance().getConnection();){
            String sql = "SELECT usuario_id, COUNT(*) as total FROM prestamo GROUP BY usuario_id ORDER BY total DESC LIMIT 3";
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            List<Integer> ids = new ArrayList<>();

            while(rs.next()) {
                int usuarioId = rs.getInt("usuario_id");
                ids.add(usuarioId);
            }
            return ids;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}