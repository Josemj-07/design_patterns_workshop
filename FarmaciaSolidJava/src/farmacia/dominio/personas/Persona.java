package farmacia.dominio.personas;

public abstract class Persona {
    private final String nombre;
    private final String cedula;
    private final String telefono;
    private final String correo;

    protected Persona(String nombre, String cedula, String telefono, String correo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre invalido");
        }
        if (cedula == null || cedula.isBlank() || !cedula.replace("-", "").matches("\\d{10}")) {
            throw new IllegalArgumentException("Cedula invalida");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Telefono invalido");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Correo invalido");
        }
        this.nombre = nombre.trim();
        this.cedula = cedula.trim();
        this.telefono = telefono.trim();
        this.correo = correo.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
}
