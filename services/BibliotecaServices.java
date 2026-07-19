package BibliotecaDigital.services;

import BibliotecaDigital.dao.LibroDAO;
import BibliotecaDigital.dao.PrestamoDAO;
import BibliotecaDigital.dao.UsuarioDAO;
import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Prestamo;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaServices {

    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;
    private PrestamoDAO prestamoDAO;

    public BibliotecaServices(LibroDAO libroDAO, UsuarioDAO usuarioDAO, PrestamoDAO prestamoDAO) {
        this.libroDAO = libroDAO;
        this.usuarioDAO = usuarioDAO;
        this.prestamoDAO = prestamoDAO;
    }

    // ── LIBROS ──────────────────────────────────────────

    public List<Libro> listarTodosLosLibros() {
        return libroDAO.listarTodos();
    }

    public void agregarLibro(Libro libro) {
        if (!libroDAO.buscarPorTitulo(libro.getTitulo()).isEmpty()) {
            throw new LibroYaExisteException("Ya existe un libro con ese título");
        }
        libroDAO.crear(libro);
    }

    public void modificarLibro(Libro libro) {
        Libro libroExistente = libroDAO.leer(libro.getId());
        if (libroExistente == null || !libroExistente.isActivo()) {
            throw new LibroNoActivoException("El libro no existe o está dado de baja");
        }
        libroDAO.actualizar(libro);
    }

    public void eliminarLibro(int id) {
        Libro libro = libroDAO.leer(id);
        if (!libro.isActivo()) {
            throw new LibroNoActivoException("El libro ya está dado de baja");
        }
        libroDAO.eliminar(id);
    }

    public Libro buscarLibroPorTitulo(String titulo) {
        List<Libro> libros = libroDAO.buscarPorTitulo(titulo);
        return libros.isEmpty() ? null : libros.get(0);
    }

    // ── USUARIOS ─────────────────────────────────────────

    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuarioDAO.buscarPorUsername(usuario.getUsername()) != null) {
            throw new UsuarioYaExisteException("Ya existe una cuenta con ese usuario");
        }
        usuarioDAO.crear(usuario);
    }

    public void modificarUsuario(Usuario usuario) {
        Usuario usuarioExistente = usuarioDAO.leer(usuario.getId());
        if (usuarioExistente == null || !usuarioExistente.isActivo()) {
            throw new UsuarioNoDisponibleException("El usuario no existe o está dado de baja");
        }
        usuarioDAO.actualizar(usuario);
    }

    public void eliminarUsuario(int id) {
        Usuario usuario = usuarioDAO.leer(id);
        if (!usuario.isActivo()) {
            throw new UsuarioNoActivoException("El usuario ya está dado de baja");
        }
        usuarioDAO.eliminar(id);
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        return usuarioDAO.buscarPorUsername(username);
    }

    // ── PRÉSTAMOS ────────────────────────────────────────

    public List<Prestamo> listarPrestamosActivos() {
        return prestamoDAO.listarActivos();
    }

    public Prestamo buscarPrestamoPorId(int id) {
        return prestamoDAO.leer(id);
    }

    public void prestarLibro(Libro libro, Usuario usuario) {
        Libro libroExistente = libroDAO.leer(libro.getId());
        Usuario usuarioExistente = usuarioDAO.leer(usuario.getId());
        if (libroExistente == null || !libroExistente.isActivo() || !libroExistente.estaDisponible()) {
            throw new LibroNoDisponibleException("El libro no está disponible");
        }
        if (usuarioExistente == null || !usuarioExistente.isActivo()) {
            throw new UsuarioNoDisponibleException("El usuario no está disponible");
        }
        Prestamo prestamo = usuario.generarPrestamo(libro);
        libro.prestar();
        usuario.setPrestamosActuales(usuario.getPrestamosActuales() + 1);
        usuario.setTotalPrestamos(usuario.getTotalPrestamos() + 1);
        prestamoDAO.crear(prestamo);
        libroDAO.actualizar(libro);
        usuarioDAO.actualizar(usuario);
    }

    public void devolverLibro(Prestamo prestamo) {
        Prestamo prestamoExistente = prestamoDAO.leer(prestamo.getId());
        if (prestamoExistente == null || prestamoExistente.isFinalizado()) {
            throw new PrestamoNoActivoException("El préstamo ya fue finalizado");
        }
        prestamo.finalizar();
        prestamo.getUsuario().setPrestamosActuales(prestamo.getUsuario().getPrestamosActuales() - 1);
        prestamo.getLibro().devolver();
        prestamoDAO.actualizar(prestamo);
        libroDAO.actualizar(prestamo.getLibro());
        usuarioDAO.actualizar(prestamo.getUsuario());
    }

    // ── REPORTES ─────────────────────────────────────────

    public List<Libro> librosMasSolicitados() {
        List<Integer> ids = prestamoDAO.librosMasSolicitados();
        List<Libro> libros = new ArrayList<>();
        for (int id : ids) {
            libros.add(libroDAO.leer(id));
        }
        return libros;
    }

    public List<Usuario> usuariosConMasPrestamos() {
        List<Integer> ids = prestamoDAO.usuariosConMasPrestamos();
        List<Usuario> usuarios = new ArrayList<>();
        for (int id : ids) {
            usuarios.add(usuarioDAO.leer(id));
        }
        return usuarios;
    }

    public List<Prestamo> listarPrestamosActivosPorUsuario(int usuarioId) {
        return prestamoDAO.listarActivosPorUsuario(usuarioId);
    }
}
