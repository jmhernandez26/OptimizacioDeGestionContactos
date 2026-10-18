
package modelo;

public class Persona {

    private String nombre;
    private String telefono;
    private String correo;
    private String categoria;
    private boolean favorito;

    public Persona(String nombre, String telefono,
                   String correo, String categoria,
                   boolean favorito) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.categoria = categoria;
        this.favorito = favorito;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}

