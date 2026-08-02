package BibliotecaDigital.views.panels;

import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.util.PasswordUtil;
import BibliotecaDigital.views.VentanaPrincipal;
import javax.swing.*;
import java.awt.*;

public class PanelLogin extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private VentanaPrincipal ventana;
    private BibliotecaServices servicios;

    public PanelLogin(VentanaPrincipal ventana, BibliotecaServices servicios) {
        this.ventana = ventana;
        this.servicios = servicios;

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");

        add(new JLabel("Usuario:"));
        add(txtUsername);
        add(new JLabel("Contraseña:"));
        add(txtPassword);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            Usuario usuario = servicios.buscarUsuarioPorUsername(username);
            if (usuario == null || !PasswordUtil.verify(password, usuario.getPassword())) {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            } else if (usuario.getRol().equals("ADMIN")) {
                ventana.cambiarPanel(new PanelMenuAdmin(servicios, ventana, usuario));
            } else {
                ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuario));
            }
        });
    }
}
