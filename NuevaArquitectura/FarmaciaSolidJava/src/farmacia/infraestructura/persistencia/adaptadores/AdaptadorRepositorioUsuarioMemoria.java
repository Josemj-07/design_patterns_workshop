package farmacia.infraestructura.persistencia.adaptadores;

import farmacia.dominio.personas.Usuario;
import farmacia.dominio.puertos.IRepositorioUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdaptadorRepositorioUsuarioMemoria implements IRepositorioUsuario {
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
