package BibliotecaDigital.views.panels.admin;

import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Prestamo;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.LibroNoDisponibleException;
import BibliotecaDigital.services.exceptions.PrestamoNoActivoException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuAdmin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionPrestamo extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    private JComboBox<String> cmbUsuario;
    private JComboBox<String> cmbLibro;

    private JButton btnGenerarPrestamo;
    private JButton btnDevolverPrestamo;
    private JButton btnVolver;

    public PanelGestionPrestamo(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarComboBoxes();
        cargarTabla();
        configurarAcciones();
    }

    private void initComponentes() {
        String[] columnas = {"ID", "Usuario", "Libro", "Fecha Inicio", "Fecha Devolución", "Retrasado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPrestamos = new JTable(modeloTabla);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTabla = new JScrollPane(tablaPrestamos);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del préstamo"));
        cmbUsuario = new JComboBox<>();
        cmbLibro   = new JComboBox<>();
        panelCampos.add(new JLabel("Usuario:")); panelCampos.add(cmbUsuario);
        panelCampos.add(new JLabel("Libro:"));   panelCampos.add(cmbLibro);
        JPanel panelEast = new JPanel(new BorderLayout());
        panelEast.add(panelCampos, BorderLayout.NORTH);
        add(panelEast, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGenerarPrestamo  = new JButton("Generar Préstamo");
        btnDevolverPrestamo = new JButton("Devolver Préstamo");
        btnVolver           = new JButton("Volver");
        panelBotones.add(btnGenerarPrestamo);
        panelBotones.add(btnDevolverPrestamo);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarComboBoxes() {
        cmbUsuario.removeAllItems();
        cmbLibro.removeAllItems();
        for (Usuario u : servicios.listarTodosLosUsuarios()) { cmbUsuario.addItem(u.getUsername()); }
        for (Libro l : servicios.listarTodosLosLibros()) { cmbLibro.addItem(l.getTitulo()); }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Prestamo> prestamos = servicios.listarPrestamosActivos();
        for (Prestamo p : prestamos) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getUsuario().getUsername(), p.getLibro().getTitulo(),
                p.getFechaInicio(), p.getFechaDevolucion(), p.isRetrasado()
            });
        }
    }

    private void configurarAcciones() {
        btnGenerarPrestamo.addActionListener(e -> {
            Usuario usuario = servicios.buscarUsuarioPorUsername((String) cmbUsuario.getSelectedItem());
            Libro libro = servicios.buscarLibroPorTitulo((String) cmbLibro.getSelectedItem());
            if (usuario == null || libro == null) {
                JOptionPane.showMessageDialog(null, "Usuario o libro no encontrado"); return;
            }
            try {
                servicios.prestarLibro(libro, usuario);
                cargarTabla(); cargarComboBoxes();
            } catch (LibroNoDisponibleException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        btnDevolverPrestamo.addActionListener(e -> {
            int fila = tablaPrestamos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un préstamo"); return; }
            int idPrestamo = (int) modeloTabla.getValueAt(fila, 0);
            Prestamo prestamo = servicios.buscarPrestamoPorId(idPrestamo);
            if (prestamo != null) {
                try {
                    servicios.devolverLibro(prestamo);
                    cargarTabla();
                } catch (PrestamoNoActivoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuAdmin(servicios, ventana, usuarioActual))
        );
    }
}
