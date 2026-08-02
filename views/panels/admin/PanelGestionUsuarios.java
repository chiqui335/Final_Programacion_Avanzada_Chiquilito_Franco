package BibliotecaDigital.views.panels.admin;

import BibliotecaDigital.model.Admin;
import BibliotecaDigital.model.Usuario;
import BibliotecaDigital.model.UsuarioRegular;
import BibliotecaDigital.services.BibliotecaServices;
import BibliotecaDigital.services.exceptions.UsuarioNoActivoException;
import BibliotecaDigital.services.exceptions.UsuarioNoDisponibleException;
import BibliotecaDigital.services.exceptions.UsuarioYaExisteException;
import BibliotecaDigital.views.VentanaPrincipal;
import BibliotecaDigital.views.panels.PanelMenuAdmin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionUsuarios extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTextField txtNombre;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtDni;
    private JTextField txtEmail;
    private JComboBox<String> rol;

    private JButton agregar;
    private JButton modificar;
    private JButton eliminar;
    private JButton volver;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public PanelGestionUsuarios(BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual) {
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarTabla();
        limpiarCampos();
        configurarAcciones();
    }

    private void initComponentes() {
        String[] columnas = {"Nombre", "Username", "DNI", "Email", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) return;
            txtNombre.setText((String) modeloTabla.getValueAt(fila, 0));
            txtUsername.setText((String) modeloTabla.getValueAt(fila, 1));
            txtPassword.setText("");
            txtDni.setText((String) modeloTabla.getValueAt(fila, 2));
            txtEmail.setText((String) modeloTabla.getValueAt(fila, 3));
            rol.setSelectedItem(modeloTabla.getValueAt(fila, 4));
        });
        scrollTabla = new JScrollPane(tablaUsuarios);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        txtNombre   = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtDni      = new JTextField();
        txtEmail    = new JTextField();
        rol = new JComboBox<>(new String[]{"REGULAR", "ADMIN"});

        panelCampos.add(new JLabel("Nombre:")); panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Usuario:")); panelCampos.add(txtUsername);
        panelCampos.add(new JLabel("Contraseña (vacío = no cambiar):")); panelCampos.add(txtPassword);
        panelCampos.add(new JLabel("DNI:")); panelCampos.add(txtDni);
        panelCampos.add(new JLabel("Email:")); panelCampos.add(txtEmail);
        panelCampos.add(new JLabel("Rol:")); panelCampos.add(rol);
        JPanel panelEast = new JPanel(new BorderLayout());
        panelEast.add(panelCampos, BorderLayout.NORTH);
        add(panelEast, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        agregar  = new JButton("Agregar");
        modificar = new JButton("Modificar");
        eliminar  = new JButton("Eliminar");
        volver    = new JButton("Volver");
        panelBotones.add(agregar);
        panelBotones.add(modificar);
        panelBotones.add(eliminar);
        panelBotones.add(volver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = servicios.listarTodosLosUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{u.getNombre(), u.getUsername(), u.getDni(), u.getEmail(), u.getRol()});
        }
    }

    private void limpiarCampos() {
        txtNombre.setText(""); txtUsername.setText(""); txtPassword.setText("");
        txtDni.setText(""); txtEmail.setText(""); rol.setSelectedIndex(0);
    }

    private void configurarAcciones() {
        agregar.addActionListener(e -> {
            String rolSeleccionado = (String) rol.getSelectedItem();
            Usuario nuevoUsuario = rolSeleccionado.equals("ADMIN")
                ? new Admin(0, txtNombre.getText().trim(), txtUsername.getText().trim(), txtPassword.getText().trim(), txtDni.getText().trim(), txtEmail.getText().trim(), 0, 0, false)
                : new UsuarioRegular(0, txtNombre.getText().trim(), txtUsername.getText().trim(), txtPassword.getText().trim(), txtDni.getText().trim(), txtEmail.getText().trim(), 0, 0, false);
            try {
                servicios.agregarUsuario(nuevoUsuario);
                cargarTabla(); limpiarCampos();
            } catch (UsuarioYaExisteException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        modificar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un usuario"); return; }
            String username = (String) modeloTabla.getValueAt(fila, 1);
            Usuario u = servicios.buscarUsuarioPorUsername(username);
            if (u != null) {
                String nombre = txtNombre.getText().trim();
                String password = txtPassword.getText().trim();
                String dni = txtDni.getText().trim();
                String email = txtEmail.getText().trim();
                if (!nombre.isEmpty()) u.setNombre(nombre);
                u.setPassword(password);
                if (!dni.isEmpty()) u.setDni(dni);
                if (!email.isEmpty()) u.setEmail(email);
                u.setRol((String) rol.getSelectedItem());
                try {
                    servicios.modificarUsuario(u);
                    cargarTabla(); limpiarCampos();
                } catch (UsuarioNoDisponibleException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        eliminar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un usuario"); return; }
            String username = (String) modeloTabla.getValueAt(fila, 1);
            Usuario u = servicios.buscarUsuarioPorUsername(username);
            if (u != null) {
                try {
                    servicios.eliminarUsuario(u.getId());
                    cargarTabla(); limpiarCampos();
                } catch (UsuarioNoActivoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        volver.addActionListener(e ->
            ventana.cambiarPanel(new PanelMenuAdmin(servicios, ventana, usuarioActual))
        );
    }
}
