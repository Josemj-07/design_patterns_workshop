package farmacia.aplicacion.puertos;

import farmacia.dominio.personas.Usuario;

import java.util.Optional;

public interface IRepositorioUsuario {
    void agregar(Usuario usuario);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);
}
