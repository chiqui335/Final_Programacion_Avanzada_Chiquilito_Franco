import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelMenuUsuario extends JPanel {
    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    public PanelMenuUsuario (BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual){
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        this.servicios = servicios;

        JButton btnVerPrestamos  = new JButton("Ver Prestamos");
        JButton btnGenerarPrestamo  = new JButton("Generar Préstamo");
        JButton btnGestionarPerfil  = new JButton("Modificar Perfil");

        add(btnVerPrestamos);
        add(btnGenerarPrestamo);
        add(btnGestionarPerfil);

        btnVerPrestamos.addActionListener(e -> {
            ventana.cambiarPanel(new PanelVerPrestamos(ventana, servicios));
        });


        btnGenerarPrestamo.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGenerarPrestamo(ventana, servicios));

        });


        btnGestionarPerfil.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGestionarPerfil(ventana, servicios));

        });
    }

}tambie