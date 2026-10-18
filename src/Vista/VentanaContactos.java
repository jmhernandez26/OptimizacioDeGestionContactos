
package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaContactos extends JFrame {

    public JTextField txtNombre;
    public JTextField txtTelefono;
    public JTextField txtCorreo;
    public JTextField txtBuscar;

    public JComboBox<String> cmbCategoria;
    public JCheckBox chbFavorito;

    public JButton btnAgregar;
    public JButton btnEditar;
    public JButton btnEliminar;
    public JButton btnLimpiar;
    public JButton btnExportar;

    public JTable tablaContactos;
    public DefaultTableModel modeloTabla;
    public JProgressBar barraProgreso;

    public JLabel lblTotal;
    public JLabel lblFavoritos;
    public JLabel lblFamilia;
    public JLabel lblAmigos;
    public JLabel lblTrabajo;

    public VentanaContactos() {
        setTitle("Gestión de Contactos");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab("Contactos", crearPanelContactos());
        pestanas.addTab("Estadísticas", crearPanelEstadisticas());

        add(pestanas);
    }

    private JPanel crearPanelContactos() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // Formulario de datos
        JPanel formulario = new JPanel(new GridLayout(5, 4, 8, 8));
        formulario.setBorder(
            BorderFactory.createTitledBorder("Datos del contacto")
        );

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();

        cmbCategoria = new JComboBox<>(
            new String[]{"Familia", "Amigos", "Trabajo"}
        );

        chbFavorito = new JCheckBox("Es favorito");

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);

        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Categoría:"));
        formulario.add(cmbCategoria);

        formulario.add(new JLabel("Favorito:"));
        formulario.add(chbFavorito);
        formulario.add(new JLabel(""));
        formulario.add(new JLabel(""));

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnExportar = new JButton("Exportar CSV");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnExportar);

        // Buscador
        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 8));
        txtBuscar = new JTextField();
        panelBusqueda.add(
            new JLabel("Buscar por nombre, teléfono o correo:"),
            BorderLayout.WEST
        );
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

        // Tabla de contactos
        modeloTabla = new DefaultTableModel(
            new Object[]{
                "Nombre", "Teléfono", "Correo",
                "Categoría", "Favorito"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaContactos = new JTable(modeloTabla);
        tablaContactos.setAutoCreateRowSorter(true);
        tablaContactos.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        tablaContactos.setRowHeight(24);

        JScrollPane scrollTabla = new JScrollPane(tablaContactos);

        // Barra de progreso
        barraProgreso = new JProgressBar();
        barraProgreso.setStringPainted(true);
        barraProgreso.setMinimum(0);
        barraProgreso.setMaximum(100);
        barraProgreso.setValue(0);
        barraProgreso.setString("Contactos cargados: 0");

        // Distribución de la ventana
        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(botones, BorderLayout.SOUTH);

        JPanel panelInferior = new JPanel(new BorderLayout(8, 8));
        panelInferior.add(panelBusqueda, BorderLayout.NORTH);
        panelInferior.add(scrollTabla, BorderLayout.CENTER);
        panelInferior.add(barraProgreso, BorderLayout.SOUTH);

        principal.add(panelSuperior, BorderLayout.NORTH);
        principal.add(panelInferior, BorderLayout.CENTER);

        return principal;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        JLabel titulo = new JLabel("Estadísticas de contactos");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        lblTotal = new JLabel("Total de contactos: 0");
        lblFavoritos = new JLabel("Contactos favoritos: 0");
        lblFamilia = new JLabel("Contactos de familia: 0");
        lblAmigos = new JLabel("Contactos de amigos: 0");
        lblTrabajo = new JLabel("Contactos de trabajo: 0");

        panel.add(titulo);
        panel.add(lblTotal);
        panel.add(lblFavoritos);
        panel.add(lblFamilia);
        panel.add(lblAmigos);
        panel.add(lblTrabajo);

        return panel;
    }

    public void actualizarEstadisticas(
            int total, int favoritos,
            int familia, int amigos, int trabajo) {

        lblTotal.setText("Total de contactos: " + total);
        lblFavoritos.setText("Contactos favoritos: " + favoritos);
        lblFamilia.setText("Contactos de familia: " + familia);
        lblAmigos.setText("Contactos de amigos: " + amigos);
        lblTrabajo.setText("Contactos de trabajo: " + trabajo);
    }
}
