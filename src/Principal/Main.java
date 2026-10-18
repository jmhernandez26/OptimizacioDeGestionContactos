package principal;

import controlador.LogicaContactos;
import vista.VentanaContactos;

public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando Gestión de Contactos...");

        VentanaContactos vista = new VentanaContactos();

        new LogicaContactos(vista);

        vista.setVisible(true);

        System.out.println("Ventana abierta correctamente.");
    }
}