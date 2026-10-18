
package modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private final Path archivo = Paths.get(
        System.getProperty("user.home"),
        "GestionContactos",
        "datosContactos.csv"
    );

    public PersonaDAO() {
        try {
            Files.createDirectories(archivo.getParent());

            if (Files.notExists(archivo)) {
                try (BufferedWriter bw = Files.newBufferedWriter(
                        archivo, StandardCharsets.UTF_8)) {
                    bw.write("Nombre;Telefono;Correo;Categoria;Favorito");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                "No se pudo preparar el archivo de contactos.", e);
        }
    }

    public void guardarContactos(List<Persona> contactos)
            throws IOException {

        try (BufferedWriter bw = Files.newBufferedWriter(
                archivo, StandardCharsets.UTF_8)) {

            bw.write("Nombre;Telefono;Correo;Categoria;Favorito");
            bw.newLine();

            for (Persona p : contactos) {
                bw.write(codificar(p.getNombre()) + ";"
                    + codificar(p.getTelefono()) + ";"
                    + codificar(p.getCorreo()) + ";"
                    + codificar(p.getCategoria()) + ";"
                    + p.isFavorito());
                bw.newLine();
            }
        }
    }

    public List<Persona> cargarContactos() throws IOException {
        List<Persona> contactos = new ArrayList<>();

        if (Files.notExists(archivo)) {
            return contactos;
        }

        try (BufferedReader br = Files.newBufferedReader(
                archivo, StandardCharsets.UTF_8)) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    if (linea.startsWith("Nombre;")) {
                        continue;
                    }
                }

                String[] datos = linea.split(";", -1);

                if (datos.length == 5) {
                    contactos.add(new Persona(
                        decodificar(datos[0]),
                        decodificar(datos[1]),
                        decodificar(datos[2]),
                        decodificar(datos[3]),
                        Boolean.parseBoolean(datos[4])
                    ));
                }
            }
        }

        return contactos;
    }

    private String codificar(String texto) {
        if (texto == null) {
            texto = "";
        }

        return texto.replace("%", "%25")
                    .replace(";", "%3B")
                    .replace("\r", "%0D")
                    .replace("\n", "%0A");
    }

    private String decodificar(String texto) {
        return texto.replace("%0A", "\n")
                    .replace("%0D", "\r")
                    .replace("%3B", ";")
                    .replace("%25", "%");
    }
}
