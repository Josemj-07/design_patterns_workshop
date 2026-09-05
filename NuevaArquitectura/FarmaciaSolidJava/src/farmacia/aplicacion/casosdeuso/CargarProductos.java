package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IFabricaProducto;
import farmacia.aplicacion.puertos.IFuenteTexto;
import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.dominio.catalogo.Producto;

import java.util.List;
import java.util.Optional;

public class CargarProductos {
    private final IRepositorioProducto repositorioProducto;
    private final IFabricaProducto fabricaProducto;
    private final IFuenteTexto fuenteTexto;

    public CargarProductos(
            IRepositorioProducto repositorioProducto,
            IFabricaProducto fabricaProducto,
            IFuenteTexto fuenteTexto) {
        this.repositorioProducto = repositorioProducto;
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
                Producto producto = fabricaProducto.crearDesdeLinea(lineaNormalizada);
                repositorioProducto.agregar(producto);
            }
            return "Productos cargados";
        } catch (RuntimeException errorCarga) {
            return errorCarga.getMessage() == null
                    ? "Error al cargar productos"
                    : errorCarga.getMessage();
        }
    }
}
