package BibliotecaDigital.views.panels;

import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.usuario.PanelVerPrestamos;
import BibliotecaDigital.views.panels.usuario.PanelGenerarPrestamo;
import BibliotecaDigital.views.panels.usuario.PanelGestionPerfil;
import javax.swing.*;

public class PanelMenuUsuario extends JPanel {
    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    public PanelMenuUsuario(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;

        JButton btnVerPrestamos    = new JButton("Ver Prestamos");
        JButton btnGenerarPrestamo = new JButton("Generar Préstamo");
        JButton btnGestionarPerfil = new JButton("Modificar Perfil");
        JButton btnCerrarSesion    = new JButton("Cerrar Sesión");

        add(btnVerPrestamos);
        add(btnGenerarPrestamo);
        add(btnGestionarPerfil);
        add(btnCerrarSesion);

        btnVerPrestamos.addActionListener(e ->
            ventana.cambiarPanel(new PanelVerPrestamos(servicios, ventana, usuarioActual))
        );
        btnGenerarPrestamo.addActionListener(e ->
            ventana.cambiarPanel(new PanelGenerarPrestamo(servicios, ventana, usuarioActual))
        );
        btnGestionarPerfil.addActionListener(e ->
            ventana.cambiarPanel(new PanelGestionPerfil(servicios, ventana, usuarioActual))
        );
        btnCerrarSesion.addActionListener(e ->
            ventana.cambiarPanel(new PanelLogin(ventana, servicios))
        );
    }
}
