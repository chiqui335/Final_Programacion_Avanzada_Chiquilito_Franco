package BibliotecaDigital.views.panels.usuario;

import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.UsuarioNoDisponibleException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuUsuario;
import javax.swing.*;
import java.awt.*;

public class PanelGestionPerfil extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JTextField txtDni;

    private JButton btnModificar;
    private JButton btnVolver;

    public PanelGestionPerfil(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarDatosUsuario();
        configurarAcciones();
    }

    private void initComponentes() {
        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        txtNombre   = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtEmail    = new JTextField();
        txtDni      = new JTextField();

        panelCampos.add(new JLabel("Nombre:")); panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Username:")); panelCampos.add(txtUsername);
        panelCampos.add(new JLabel("Password:")); panelCampos.add(txtPassword);
        panelCampos.add(new JLabel("Email:")); panelCampos.add(txtEmail);
        panelCampos.add(new JLabel("DNI:")); panelCampos.add(txtDni);
        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnModificar = new JButton("Modificar");
        btnVolver    = new JButton("Volver");
        panelBotones.add(btnModificar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatosUsuario() {
        txtNombre.setText(usuarioActual.getNombre());
        txtUsername.setText(usuarioActual.getUsername());
        txtPassword.setText(usuarioActual.getPassword());
        txtEmail.setText(usuarioActual.getEmail());
        txtDni.setText(usuarioActual.getDni());
    }

    private void configurarAcciones() {
        btnModificar.addActionListener(e -> {
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setUsername(txtUsername.getText());
            usuarioActual.setPassword(new String(txtPassword.getPassword()));
            usuarioActual.setEmail(txtEmail.getText());
            usuarioActual.setDni(txtDni.getText());
            try {
                servicios.modificarUsuario(usuarioActual);
                JOptionPane.showMessageDialog(null, "Perfil modificado correctamente");
                ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuarioActual));
            } catch (UsuarioNoDisponibleException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        btnVolver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuUsuario(servicios, ventana, usuarioActual))
        );
    }
}
