package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IFuenteTexto;
import farmacia.aplicacion.puertos.IRepositorioCliente;
import farmacia.dominio.personas.Cliente;

import java.util.List;
import java.util.Optional;

public class CargarClientes {
    private final IRepositorioCliente repositorioCliente;
    private final IFuenteTexto fuenteTexto;

    public CargarClientes(IRepositorioCliente repositorioCliente, IFuenteTexto fuenteTexto) {
        this.repositorioCliente = repositorioCliente;
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
                Cliente cliente = new Cliente(nombre, cedula, telefono, correo);
                repositorioCliente.agregar(cliente);
            }
            return "Clientes cargados";
        } catch (RuntimeException errorCarga) {
            return errorCarga.getMessage() == null
                    ? "Error al cargar clientes"
                    : errorCarga.getMessage();
        }
    }
}
