package farmacia.dominio.puertos;

public interface IAutenticacion {
    boolean login(String nombreUsuario, String contrasena);
}
