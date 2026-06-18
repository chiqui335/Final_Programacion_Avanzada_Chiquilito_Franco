package views.panels.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionLibros extends JPanel {

    private BibliotecaServices servicios;
    private VentanaPrincipal ventana;
    private Usuario usuarioActual;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtTipo;
    private JCheckBox chkTapaDura;
    private JSpinner spnCantidadTotal;

    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnVolver;

    public PanelGestionLibros(VentanaPrincipal ventana, BibliotecaServices servicios, Usuario usuarioActual) {
        this.ventana = ventana;
        this.servicios = servicios;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(10, 10));
        initComponentes();
        cargarTabla();
        configurarAcciones();
    }

    private void initComponentes() {
        String[] columnas = {"Título", "Autor", "Tipo", "Tapa Dura", "Cantidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTabla = new JScrollPane(tablaLibros);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del libro"));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtTipo = new JTextField();
        chkTapaDura = new JCheckBox();
        spnCantidadTotal = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        panelCampos.add(new JLabel("Título:"));
        panelCampos.add(txtTitulo);
        panelCampos.add(new JLabel("Autor:"));
        panelCampos.add(txtAutor);
        panelCampos.add(new JLabel("Tipo:"));
        panelCampos.add(txtTipo);
        panelCampos.add(new JLabel("Tapa dura:"));
        panelCampos.add(chkTapaDura);
        panelCampos.add(new JLabel("Cantidad total:"));
        panelCampos.add(spnCantidadTotal);

        add(panelCampos, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new J