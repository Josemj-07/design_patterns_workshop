package farmacia.infraestructura.auth;

import farmacia.aplicacion.puertos.IAutenticacion;
import farmacia.aplicacion.puertos.IRepositorioUsuario;

/**
 * Valida credenciales buscando solo el usuario indicado (no carga toda la lista).
 */
public class AutenticacionPorCredenciales implements IAutenticacion {
    private final IRepositorioUsuario repositorioUsuario;

    public AutenticacionPorCredenciales(IRepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public boolean login(String nombreUsuario, String contrasena) {
        return repositorioUsuario
                .buscarPorNombreUsuario(nombreUsuario)
                .map(usuario -> usuario.credencialesCoinciden(nombreUsuario, contrasena))
                .orElse(false);
    }
}
