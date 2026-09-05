package farmacia.dominio.personas;

public class Usuario extends Persona {
    private final String nombreUsuario;
    private final String contrasena;

    public Usuario(
            String nombre,
            String cedula,
            String telefono,
            String correo,
            String nombreUsuario,
            String contrasena) {
        super(nombre, cedula, telefono, correo);
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("Usuario invalido");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Contrasena invalida");
        }
        this.nombreUsuario = nombreUsuario.trim();
        this.contrasena = contrasena;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean credencialesCoinciden(String nombreUsuarioIngresado, String contrasenaIngresada) {
        return nombreUsuario.equals(nombreUsuarioIngresado)
                && contrasena.equals(contrasenaIngresada);
    }
}
