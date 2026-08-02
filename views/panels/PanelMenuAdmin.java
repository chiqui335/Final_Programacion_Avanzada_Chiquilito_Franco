package BibliotecaDigital.views.panels;

import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.admin.PanelGestionLibros;
import BibliotecaDigital.views.panels.admin.PanelGestionUsuarios;
import BibliotecaDigital.views.panels.admin.PanelGestionPrestamo;
import BibliotecaDigital.views.panels.admin.PanelGestionReportes;
import javax.swing.*;

public class PanelMenuAdmin extends JPanel {
    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    public PanelMenuAdmin(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;

        JButton btnGestionarLibros   = new JButton("Gestionar Libros");
        JButton btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        JButton btnGenerarPrestamo   = new JButton("Generar Préstamo");
        JButton btnReportes          = new JButton("Ver Reportes");
        JButton btnCerrarSesion      = new JButton("Cerrar Sesión");

        add(btnGestionarLibros);
        add(btnGestionarUsuarios);
        add(btnGenerarPrestamo);
        add(btnReportes);
        add(btnCerrarSesion);

        btnGestionarLibros.addActionListener(e ->
            ventana.cambiarPanel(new PanelGestionLibros(ventana, servicios, usuarioActual))
        );
        btnGestionarUsuarios.addActionListener(e ->
            ventana.cambiarPanel(new PanelGestionUsuarios(servicios, ventana, usuarioActual))
        );
        btnGenerarPrestamo.addActionListener(e ->
            ventana.cambiarPanel(new PanelGestionPrestamo(servicios, ventana, usuarioActual))
        );
        btnReportes.addActionListener(e ->
            ventana.cambiarPanel(new PanelGestionReportes(servicios, ventana, usuarioActual))
        );
        btnCerrarSesion.addActionListener(e ->
            ventana.cambiarPanel(new PanelLogin(ventana, servicios))
        );
    }
}
