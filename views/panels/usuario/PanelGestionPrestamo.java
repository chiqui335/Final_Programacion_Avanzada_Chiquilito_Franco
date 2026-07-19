package BibliotecaDigital.views.panels.usuario;

import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.VentanaPrincipal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelGestionPrestamo extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public PanelGestionPrestamo(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
    }
}
