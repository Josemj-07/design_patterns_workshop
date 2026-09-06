package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.puertos.IFabricaPagable;
import farmacia.dominio.puertos.IFuenteTexto;
import farmacia.dominio.puertos.IRepositorioPagable;

import java.util.List;
import java.util.Optional;

public class CargarPagables {
    private final IRepositorioPagable repositorioPagable;
    private final IFabricaPagable fabricaProducto;
    private final IFuenteTexto fuenteTexto;

    public CargarPagables(
            IRepositorioPagable repositorioProducto,
            IFabricaPagable fabricaProducto,
            IFuenteTexto fuenteTexto) {
        this.repositorioPagable = repositorioProducto;
        this.fabricaProducto = fabricaProducto;
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
                IPagable pagable = fabricaProducto.crearDesdeLinea(lineaNormalizada);
                repositorioPagable.agregar(pagable);
            }
            return "Productos cargados";
        } catch (RuntimeException errorCarga) {
            return errorCarga.getMessage() == null
                    ? "Error al cargar productos"
                    : errorCarga.getMessage();
        }
    }
}
