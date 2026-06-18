import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelLogin extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private VentanaPrincipal ventana;

    private UsuarioDAO usuarioDAO;

    public PanelLogin (VentanaPrincipal ventana, UsuarioDAO usuarioDAO){
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesion");
        this.ventana = ventana;

        this.usuarioDAO = usuarioDAO;

        add(new JLabel ("Usuario:"));
        add(txtUsername);
        add(new JLabel("Contraseña"));
        add(txtPassword);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        Usuario usuario = usuarioDAO.buscarPorUsername(username);

        if(usuario == null || !usuario.getPassword().equals(password)){
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        } else if (usuario.getRol().equals("ADMIN")) {
            ventana.cambiarPanel(new PanelMenuAdmin(ventana, usuario));
        } else {
            ventana.cambiarPanel(new PanelMenuUsuario(ventana, usuario));
        }
    });
    }


    

}