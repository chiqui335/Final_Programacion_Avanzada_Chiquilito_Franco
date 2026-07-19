package BibliotecaDigital.views.panels.usuario;

import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.LibroNoDisponibleException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuUsuario;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelGenerarPrestamo extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JComboBox<String> cmbLibro;
    private JButton btnGenerar;
    private JButton btnVolver;

    public PanelGenerarPrestamo(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarLibros();
    }

    private void initComponentes() {
        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Seleccionar libro"));
        cmbLibro = new JComboBox<>();
        panelCampos.add(new JLabel("Libro:"));
        panelCampos.add(cmbLibro);
        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGenerar = new JButton("Generar Préstamo");
        btnVolver  = new JButton("Volver");
        panelBotones.add(btnGenerar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        btnGenerar.addActionListener(e -> {
            Libro libro = servicios.buscarLibroPorTitulo((String) cmbLibro.getSelectedItem());
            if (libro == null) { JOptionPane.showMessageDialog(null, "Libro no encontrado"); return; }
            try {
                servicios.prestarLibro(libro, usuarioActual);
                JOptionPane.showMessageDialog(null, "Préstamo generado correctamente");
                ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuarioActual));
            } catch (LibroNoDisponibleException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuarioActual))
        );
    }

    private void cargarLibros() {
        cmbLibro.removeAllItems();
        List<Libro> libros = servicios.listarTodosLosLibros();
        for (Libro libro : libros) {
            if (libro.estaDisponible()) cmbLibro.addItem(libro.getTitulo());
        }
    }
}
