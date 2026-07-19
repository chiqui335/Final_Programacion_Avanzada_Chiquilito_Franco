package BibliotecaDigital.views;

import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.views.panels.PanelLogin;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaPrincipal extends JFrame {
    private BibliotecaServices servicios;

    public VentanaPrincipal(BibliotecaServices servicios) {
        this.servicios = servicios;
        setTitle("Biblioteca Digital");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cambiarPanel(new PanelLogin(this, servicios));
    }

    public void cambiarPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }
}
