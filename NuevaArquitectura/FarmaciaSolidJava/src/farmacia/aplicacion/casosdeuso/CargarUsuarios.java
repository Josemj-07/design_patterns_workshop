package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IFuenteTexto;
import farmacia.aplicacion.puertos.IRepositorioUsuario;
import farmacia.dominio.personas.Usuario;

import java.util.List;
import java.util.Optional;

public class CargarUsuarios {
    private final IRepositorioUsuario repositorioUsuario;
    private final IFuenteTexto fuenteTexto;

    public CargarUsuarios(IRepositorioUsuario repositorioUsuario, IFuenteTexto fuenteTexto) {
        this.repositorioUsuario = repositorioUsuario;
        this.fuenteTexto = fuenteTexto;
    }

    public String ejecutar(String rutaArchivo) {
        try {
            Optional<List<String>> lineasDelArchivo = fuenteTexto.leerLineas(rutaArchivo);
            if (lineasDelArchivo.isEmpty()) {
                return "Archivo no encontrado";
            }
            for (String lineaCruda : lineasDelArchivo.get()) {
                if (lineaCruda == null || lineaCruda.isBlank()) {
                    continue;
                }
                String lineaNormalizada = lineaCruda.replace("\uFEFF", "").trim();
                String[] campos = lineaNormalizada.split(";");
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
            return "Usuarios cargados";
        } catch (RuntimeException errorCarga) {
            return errorCarga.getMessage() == null
                    ? "Error al cargar usuarios"
                    : errorCarga.getMessage();
        }
    }
}
