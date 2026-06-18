import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelMenuAdmin extends JPanel{
    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    public PanelMenuAdmin (BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual){
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        this.servicios = servicios;

        JButton btnGestionarLibros  = new JButton("Gestionar Libros");
        JButton btnGestionarUsuarios  = new JButton("Gestionar Usuarios");
        JButton btnGenerarPrestamo  = new JButton("Generar Préstamo");
        JButton btnReportes  = new JButton("Ver Reportes");

        add(btnGestionarLibros);
        add(btnGestionarUsuarios);
        add(btnGenerarPrestamo);
        add(btnReportes);

        btnGestionarLibros.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGestionLibros(ventana, servicios));
        });

        btnGestionarUsuarios.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGestionUsuarios(ventana, servicios));

        });

        btnGenerarPrestamo.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGestionPrestamo(ventana, servicios));

        });

        btnReportes.addActionListener(e -> {
            ventana.cambiarPanel(new PanelGestionReportes(ventana, servicios));

        });
    }
}