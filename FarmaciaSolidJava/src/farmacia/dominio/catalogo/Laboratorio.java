package farmacia.dominio.catalogo;

public class Laboratorio {
    private final String nombre;
    private final String direccion;
    private final String telefono;

    public Laboratorio(String nombre, String direccion, String telefono) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre de laboratorio no debe estar vacio");
        }
        this.nombre = nombre.trim();
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
