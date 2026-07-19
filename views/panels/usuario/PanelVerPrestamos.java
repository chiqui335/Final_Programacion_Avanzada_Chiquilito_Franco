package BibliotecaDigital.views.panels.usuario;

import BibliotecaDigital.model.Prestamo;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelVerPrestamos extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    private JButton btnVolver;
    private JButton btnDevolver;

    public PanelVerPrestamos(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarTabla();
    }

    private void initComponentes() {
        String[] columnas = {"ID", "Libro", "Fecha Inicio", "Fecha Devolución", "Retrasado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPrestamos = new JTable(modeloTabla);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTabla = new JScrollPane(tablaPrestamos);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnDevolver = new JButton("Devolver");
        btnVolver   = new JButton("Volver");
        panelBotones.add(btnDevolver);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        btnDevolver.addActionListener(e -> {
            int fila = tablaPrestamos.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un préstamo"); return; }
            int idPrestamo = (int) modeloTabla.getValueAt(fila, 0);
            Prestamo prestamo = servicios.buscarPrestamoPorId(idPrestamo);
            servicios.devolverLibro(prestamo);
            cargarTabla();
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuarioActual))
        );
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Prestamo> prestamos = servicios.listarPrestamosActivosPorUsuario(usuarioActual.getId());
        for (Prestamo p : prestamos) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getLibro().getTitulo(),
                p.getFechaInicio(), p.getFechaDevolucion(), p.isRetrasado()
            });
        }
    }
}
