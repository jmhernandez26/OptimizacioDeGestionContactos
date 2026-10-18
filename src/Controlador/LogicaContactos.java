package controlador;

import modelo.Persona;
import modelo.PersonaDAO;
import vista.VentanaContactos;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class LogicaContactos {

    private VentanaContactos vista;
    private PersonaDAO personaDAO;
    private List<Persona> contactos;

    private TableRowSorter<javax.swing.table.DefaultTableModel> ordenador;

    public LogicaContactos(VentanaContactos vista) {

        this.vista = vista;
        this.personaDAO = new PersonaDAO();
        this.contactos = new ArrayList<>();

        configurarEventos();
        configurarBusqueda();
        configurarTabla();
        configurarAtajos();
        configurarMenuContextual();

        cargarContactos();
    }

    // =====================================================
    // CONFIGURAR EVENTOS DE LOS BOTONES
    // =====================================================

    private void configurarEventos() {

        vista.btnAgregar.addActionListener(e -> agregarContacto());

        vista.btnEditar.addActionListener(e -> editarContacto());

        vista.btnEliminar.addActionListener(e -> eliminarContacto());

        vista.btnLimpiar.addActionListener(e -> limpiarCampos());

        vista.btnExportar.addActionListener(e -> exportarCSV());
    }

    // =====================================================
    // AGREGAR CONTACTO
    // =====================================================

    private void agregarContacto() {

        String nombre = vista.txtNombre.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String correo = vista.txtCorreo.getText().trim();

        String categoria =
                vista.cmbCategoria.getSelectedItem().toString();

        boolean favorito = vista.chbFavorito.isSelected();

        if (nombre.isEmpty()
                || telefono.isEmpty()
                || correo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Complete nombre, teléfono y correo.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Persona persona = new Persona(
                nombre,
                telefono,
                correo,
                categoria,
                favorito
        );

        contactos.add(persona);

        guardarContactos();
        actualizarTabla();
        limpiarCampos();

        JOptionPane.showMessageDialog(
                vista,
                "Contacto agregado correctamente."
        );
    }

    // =====================================================
    // EDITAR CONTACTO
    // =====================================================

    private void editarContacto() {

        int filaVista = vista.tablaContactos.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un contacto para editar."
            );

            return;
        }

        int filaModelo =
                vista.tablaContactos.convertRowIndexToModel(filaVista);

        Persona persona = contactos.get(filaModelo);

        String nombre = vista.txtNombre.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String correo = vista.txtCorreo.getText().trim();

        if (nombre.isEmpty()
                || telefono.isEmpty()
                || correo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Complete todos los campos."
            );

            return;
        }

        persona.setNombre(nombre);
        persona.setTelefono(telefono);
        persona.setCorreo(correo);

        persona.setCategoria(
                vista.cmbCategoria.getSelectedItem().toString()
        );

        persona.setFavorito(
                vista.chbFavorito.isSelected()
        );

        guardarContactos();
        actualizarTabla();
        limpiarCampos();

        JOptionPane.showMessageDialog(
                vista,
                "Contacto editado correctamente."
        );
    }

    // =====================================================
    // ELIMINAR CONTACTO
    // =====================================================

    private void eliminarContacto() {

        int filaVista = vista.tablaContactos.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un contacto para eliminar."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar este contacto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            int filaModelo =
                    vista.tablaContactos.convertRowIndexToModel(filaVista);

            contactos.remove(filaModelo);

            guardarContactos();
            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    vista,
                    "Contacto eliminado correctamente."
            );
        }
    }

    // =====================================================
    // LIMPIAR CAMPOS
    // =====================================================

    private void limpiarCampos() {

        vista.txtNombre.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");

        vista.cmbCategoria.setSelectedIndex(0);

        vista.chbFavorito.setSelected(false);

        vista.tablaContactos.clearSelection();

        vista.txtNombre.requestFocus();
    }

    // =====================================================
    // CARGAR CONTACTOS DEL CSV
    // =====================================================

    private void cargarContactos() {

        try {

            contactos = personaDAO.cargarContactos();

            actualizarTabla();

            vista.barraProgreso.setValue(100);

            vista.barraProgreso.setString(
                    "Contactos cargados: "
                    + contactos.size()
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al cargar los contactos: "
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // GUARDAR CONTACTOS
    // =====================================================

    private void guardarContactos() {

        try {

            personaDAO.guardarContactos(contactos);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al guardar los contactos: "
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // ACTUALIZAR TABLA
    // =====================================================

    private void actualizarTabla() {

        vista.modeloTabla.setRowCount(0);

        for (Persona persona : contactos) {

            vista.modeloTabla.addRow(
                    new Object[]{
                        persona.getNombre(),
                        persona.getTelefono(),
                        persona.getCorreo(),
                        persona.getCategoria(),
                        persona.isFavorito() ? "Sí" : "No"
                    }
            );
        }

        actualizarEstadisticas();

        vista.barraProgreso.setValue(100);

        vista.barraProgreso.setString(
                "Contactos cargados: "
                + contactos.size()
        );
    }

    // =====================================================
    // SELECCIONAR CONTACTO DE LA TABLA
    // =====================================================

    private void configurarTabla() {

        vista.tablaContactos.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            cargarContactoSeleccionado();
                        }
                    }
                }
        );
    }

    private void cargarContactoSeleccionado() {

        int filaVista =
                vista.tablaContactos.getSelectedRow();

        if (filaVista == -1) {
            return;
        }

        int filaModelo =
                vista.tablaContactos.convertRowIndexToModel(filaVista);

        Persona persona =
                contactos.get(filaModelo);

        vista.txtNombre.setText(
                persona.getNombre()
        );

        vista.txtTelefono.setText(
                persona.getTelefono()
        );

        vista.txtCorreo.setText(
                persona.getCorreo()
        );

        vista.cmbCategoria.setSelectedItem(
                persona.getCategoria()
        );

        vista.chbFavorito.setSelected(
                persona.isFavorito()
        );
    }

    // =====================================================
    // BUSCAR Y FILTRAR CONTACTOS
    // =====================================================

    private void configurarBusqueda() {

        ordenador =
                new TableRowSorter<>(vista.modeloTabla);

        vista.tablaContactos.setRowSorter(ordenador);

        vista.txtBuscar.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filtrar();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filtrar();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filtrar();
                    }
                }
        );
    }

    private void filtrar() {

        String texto =
                vista.txtBuscar.getText().trim();

        if (texto.isEmpty()) {

            ordenador.setRowFilter(null);

        } else {

            ordenador.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" + java.util.regex.Pattern.quote(texto)
                    )
            );
        }
    }

    // =====================================================
    // ESTADÍSTICAS
    // =====================================================

    private void actualizarEstadisticas() {

        int favoritos = 0;
        int familia = 0;
        int amigos = 0;
        int trabajo = 0;

        for (Persona persona : contactos) {

            if (persona.isFavorito()) {
                favoritos++;
            }

            if (persona.getCategoria()
                    .equalsIgnoreCase("Familia")) {

                familia++;

            } else if (persona.getCategoria()
                    .equalsIgnoreCase("Amigos")) {

                amigos++;

            } else if (persona.getCategoria()
                    .equalsIgnoreCase("Trabajo")) {

                trabajo++;
            }
        }

        vista.actualizarEstadisticas(
                contactos.size(),
                favoritos,
                familia,
                amigos,
                trabajo
        );
    }

    // =====================================================
    // EXPORTAR A CSV
    // =====================================================

    private void exportarCSV() {

        JFileChooser selector =
                new JFileChooser();

        selector.setDialogTitle(
                "Guardar contactos"
        );

        selector.setSelectedFile(
                new File("contactos_exportados.csv")
        );

        int opcion =
                selector.showSaveDialog(vista);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo =
                selector.getSelectedFile();

        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(archivo))) {

            bw.write(
                    "Nombre;Telefono;Correo;Categoria;Favorito"
            );

            bw.newLine();

            for (Persona persona : contactos) {

                bw.write(
                        persona.getNombre() + ";"
                        + persona.getTelefono() + ";"
                        + persona.getCorreo() + ";"
                        + persona.getCategoria() + ";"
                        + (persona.isFavorito()
                           ? "Si" : "No")
                );

                bw.newLine();
            }

            JOptionPane.showMessageDialog(
                    vista,
                    "Contactos exportados correctamente."
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo exportar el archivo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // ATAJOS DEL TECLADO
    // =====================================================

    private void configurarAtajos() {

        JRootPane root =
                vista.getRootPane();

        InputMap input =
                root.getInputMap(
                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );

        ActionMap acciones =
                root.getActionMap();

        // CTRL + N = agregar
        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_N,
                        InputEvent.CTRL_DOWN_MASK
                ),
                "agregar"
        );

        acciones.put(
                "agregar",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e) {

                        agregarContacto();
                    }
                }
        );

        // CTRL + E = editar
        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_E,
                        InputEvent.CTRL_DOWN_MASK
                ),
                "editar"
        );

        acciones.put(
                "editar",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e) {

                        editarContacto();
                    }
                }
        );

        // DELETE = eliminar
        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_DELETE, 0
                ),
                "eliminar"
        );

        acciones.put(
                "eliminar",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e) {

                        eliminarContacto();
                    }
                }
        );
    }

    // =====================================================
    // MENÚ CONTEXTUAL - CLIC DERECHO
    // =====================================================

    private void configurarMenuContextual() {

        JPopupMenu menu =
                new JPopupMenu();

        JMenuItem editar =
                new JMenuItem("Editar contacto");

        JMenuItem eliminar =
                new JMenuItem("Eliminar contacto");

        editar.addActionListener(
                e -> {
                    cargarContactoSeleccionado();
                    vista.txtNombre.requestFocus();
                }
        );

        eliminar.addActionListener(
                e -> eliminarContacto()
        );

        menu.add(editar);
        menu.add(eliminar);

        vista.tablaContactos.setComponentPopupMenu(menu);
    }
}