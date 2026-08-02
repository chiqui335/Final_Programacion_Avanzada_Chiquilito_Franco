package BibliotecaDigital.views.panels.admin;

import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Prestamo;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.PrestamoNoActivoException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuAdmin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionReportes extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaLibros;
    private DefaultTableModel modeloTablaLibros;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTablaPrestamos;
    private JButton btnDevolverPrestamo;
    private JButton btnVolver;

    public PanelGestionReportes(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarReportes();
    }

    private void initComponentes() {
        modeloTablaLibros = new DefaultTableModel(new String[]{"Título", "Autor", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaLibros = new JTable(modeloTablaLibros);
        JScrollPane scrollLibros = new JScrollPane(tablaLibros);
        scrollLibros.setBorder(BorderFactory.createTitledBorder("Libros más solicitados"));

        modeloTablaUsuarios = new DefaultTableModel(new String[]{"Nombre", "Username", "Email", "Total Préstamos"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        JScrollPane scrollUsuarios = new JScrollPane(tablaUsuarios);
        scrollUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios con más préstamos"));

        modeloTablaPrestamos = new DefaultTableModel(new String[]{"ID", "Usuario", "Libro", "Fecha Inicio", "Fecha Devolución", "Retrasado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPrestamos = new JTable(modeloTablaPrestamos);
        JScrollPane scrollPrestamos = new JScrollPane(tablaPrestamos);
        scrollPrestamos.setBorder(BorderFactory.createTitledBorder("Préstamos activos"));

        JPanel panelTablas = new JPanel(new GridLayout(3, 1, 5, 5));
        panelTablas.add(scrollLibros);
        panelTablas.add(scrollUsuarios);
        panelTablas.add(scrollPrestamos);
        add(panelTablas, BorderLayout.CENTER);

        btnDevolverPrestamo = new JButton("Devolver Préstamo");
        btnVolver = new JButton("Volver");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnDevolverPrestamo);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        btnDevolverPrestamo.addActionListener(e -> {
            int fila = tablaPrestamos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un préstamo"); return; }
            int idPrestamo = (int) modeloTablaPrestamos.getValueAt(fila, 0);
            Prestamo prestamo = servicios.buscarPrestamoPorId(idPrestamo);
            if (prestamo != null) {
                try {
                    servicios.devolverLibro(prestamo);
                    cargarReportes();
                } catch (PrestamoNoActivoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuAdmin(servicios, ventana, usuarioActual))
        );
    }

    private void cargarReportes() {
        modeloTablaLibros.setRowCount(0);
        List<Libro> libros = servicios.librosMasSolicitados();
        for (Libro libro : libros) {
            modeloTablaLibros.addRow(new Object[]{libro.getTitulo(), libro.getAutor(), libro.getTipo()});
        }
        modeloTablaUsuarios.setRowCount(0);
        List<Usuario> usuarios = servicios.usuariosConMasPrestamos();
        for (Usuario usuario : usuarios) {
            modeloTablaUsuarios.addRow(new Object[]{usuario.getNombre(), usuario.getUsername(), usuario.getEmail(), usuario.getTotalPrestamos()});
        }
        modeloTablaPrestamos.setRowCount(0);
        List<Prestamo> prestamos = servicios.listarPrestamosActivos();
        for (Prestamo prestamo : prestamos) {
            modeloTablaPrestamos.addRow(new Object[]{
                prestamo.getId(), prestamo.getUsuario().getUsername(), prestamo.getLibro().getTitulo(),
                prestamo.getFechaInicio(), prestamo.getFechaDevolucion(), prestamo.isRetrasado()
            });
        }
    }
}
