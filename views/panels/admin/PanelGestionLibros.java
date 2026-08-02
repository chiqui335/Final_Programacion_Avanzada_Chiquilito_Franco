package BibliotecaDigital.views.panels.admin;

import BibliotecaDigital.model.Libro;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.LibroNoActivoException;
import BibliotecaDigital.services.exceptions.LibroYaExisteException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuAdmin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionLibros extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtTipo;
    private JCheckBox chkTapaDura;
    private JSpinner spnCantidadTotal;

    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnVolver;

    public PanelGestionLibros(VentanaPrincipal ventana, BibliotecaServices servicios, Usuario usuarioActual) {
        this.ventana = ventana;
        this.servicios = servicios;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarTabla();
        configurarAcciones();
    }

    private void initComponentes() {
        String[] columnas = {"Título", "Autor", "Tipo", "Tapa Dura", "Disponibles", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTabla = new JScrollPane(tablaLibros);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del libro"));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtTipo = new JTextField();
        chkTapaDura = new JCheckBox();
        spnCantidadTotal = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        panelCampos.add(new JLabel("Título:")); panelCampos.add(txtTitulo);
        panelCampos.add(new JLabel("Autor:")); panelCampos.add(txtAutor);
        panelCampos.add(new JLabel("Tipo:")); panelCampos.add(txtTipo);
        panelCampos.add(new JLabel("Tapa dura:")); panelCampos.add(chkTapaDura);
        panelCampos.add(new JLabel("Cantidad total:")); panelCampos.add(spnCantidadTotal);
        JPanel panelEast = new JPanel(new BorderLayout());
        panelEast.add(panelCampos, BorderLayout.NORTH);
        add(panelEast, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAgregar   = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar  = new JButton("Eliminar");
        btnVolver    = new JButton("Volver");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Libro> libros = servicios.listarTodosLosLibros();
        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                libro.getTitulo(), libro.getAutor(), libro.getTipo(),
                libro.isTapaDura(), libro.getCantidadDisponible(), libro.getCantidadTotal()
            });
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtTipo.setText("");
        chkTapaDura.setSelected(false);
        spnCantidadTotal.setValue(0);
    }

    private void configurarAcciones() {
        btnAgregar.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String tipo = txtTipo.getText().trim();
            boolean tapaDura = chkTapaDura.isSelected();
            int cantidad = (int) spnCantidadTotal.getValue();
            Libro libro = new Libro(0, titulo, autor, tipo, tapaDura, cantidad, cantidad, true);
            try {
                servicios.agregarLibro(libro);
                cargarTabla();
                limpiarCampos();
            } catch (LibroYaExisteException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        btnModificar.addActionListener(e -> {
            int fila = tablaLibros.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un libro"); return; }
            String titulo = (String) modeloTabla.getValueAt(fila, 0);
            Libro libroExistente = servicios.buscarLibroPorTitulo(titulo);
            if (libroExistente != null) {
                libroExistente.setTitulo(txtTitulo.getText().trim());
                libroExistente.setAutor(txtAutor.getText().trim());
                libroExistente.setTipo(txtTipo.getText().trim());
                libroExistente.setTapaDura(chkTapaDura.isSelected());
                libroExistente.setCantidadTotal((int) spnCantidadTotal.getValue());
                try {
                    servicios.modificarLibro(libroExistente);
                    cargarTabla();
                    limpiarCampos();
                } catch (LibroNoActivoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaLibros.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un libro"); return; }
            String titulo = (String) modeloTabla.getValueAt(fila, 0);
            Libro libro = servicios.buscarLibroPorTitulo(titulo);
            if (libro != null) {
                try {
                    servicios.eliminarLibro(libro.getId());
                    cargarTabla();
                    limpiarCampos();
                } catch (LibroNoActivoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuAdmin(servicios, ventana, usuarioActual))
        );
    }
}
