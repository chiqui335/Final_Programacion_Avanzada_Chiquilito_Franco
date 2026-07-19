package BibliotecaDigital.views.panels.admin;

import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
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

        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 5, 5));
        panelTablas.add(scrollLibros);
        panelTablas.add(scrollUsuarios);
        add(panelTablas, BorderLayout.CENTER);

        btnVolver = new JButton("Volver");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

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
    }
}
