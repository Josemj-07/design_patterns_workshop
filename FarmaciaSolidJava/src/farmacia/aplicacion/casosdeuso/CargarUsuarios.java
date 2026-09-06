package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.personas.Usuario;
import farmacia.dominio.puertos.IFuenteTexto;
import farmacia.dominio.puertos.IRepositorioUsuario;

import java.util.List;
import java.util.Optional;

public class CargarUsuarios extends CargadorArchivos {
    private final IRepositorioUsuario repositorioUsuario;

    public CargarUsuarios(IRepositorioUsuario repositorioUsuario, IFuenteTexto fuenteTexto) {
        super(fuenteTexto);
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    protected String mostarError() {
        return "Error al cargar usuarios";
    }

    @Override
    protected String mostarMensaje() {
        return "Usuarios cargados";
    }

    @Override
    protected void crearObjeto(String[] campos) {
        String nombre = campos[0];
        String cedula = campos[1];
        String telefono = campos[2];
        String correo = campos[3];
        String nombreUsuario = campos[4];
        String contrasena = campos[5];
        Usuario usuario = new Usuario(
                nombre, cedula, telefono, correo, nombreUsuario, contrasena);
        repositorioUsuario.agregar(usuario);
    }
}
