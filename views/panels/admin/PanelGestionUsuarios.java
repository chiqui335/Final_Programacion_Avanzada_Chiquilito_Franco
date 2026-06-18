package panels.admin;

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

    private JComboBox rol;

    private JButton agregar;
    private JButton modificar;
    private JButton eliminar;
    private JButton volver;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JScrollPane scrollTabla;

    public PanelGestionUsuarios (BibliotecaServices servicios, VentanaPrincipal ventana, Usuario usuarioActual){
        this.servicios = servicios;
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;

        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarTabla();
        limpiarCampos();
        configurarAcciones();
    }

    private void initComponentes(){
        String[] columnas = {"Nombre", "Username", "Password", "Dni", "Email"};
        modeloTabla = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTabla = new JScrollPane(tablaUsuarios);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCampos = new JPanel(new GridLayout(6,2,5,5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos el usuario"));

        txtNombre = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtDni = new JTextField();
        txtEmail = new JTextField();

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsername);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtPassword);
        panelCampos.add(new JLabel("DNI:"));
        panelCampos.add(txtDni);
        panelCampos.add(new JLabel("Email:"));
        panelCampos.add(txtEmail);

        add(panelCampos, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        add(panelBotones, BorderLayout.SOUTH);

        agregar = new JButton("Agregar");
        modificar = new JButton("Modificar");
        eliminar = new JButton("Eliminar");
        volver = new JButton("Volver");

        panelBotones.add(agregar);
        panelBotones.add(modificar);
        panelBotones.add(eliminar);
        panelBotones.add(volver);


        rol = new JComboBox<>(new String[]{"REGULAR", "ADMIN"});
        panelCampos.add(new JLabel("Rol:"));
        panelCampos.add(rol);
    }
}