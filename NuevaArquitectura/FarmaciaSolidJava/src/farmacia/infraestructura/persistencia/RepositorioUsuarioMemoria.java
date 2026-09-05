package farmacia.infraestructura.persistencia;

import farmacia.aplicacion.puertos.IRepositorioUsuario;
import farmacia.dominio.personas.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioUsuarioMemoria implements IRepositorioUsuario {
    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarios.stream()
                .filter(usuario -> usuario.getNombreUsuario().equals(nombreUsuario))
                .findFirst();
    }
}
