package farmacia.infraestructura.persistencia;

import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.dominio.catalogo.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioProductoMemoria implements IRepositorioProducto {
    private final List<Producto> productos = new ArrayList<>();

    @Override
    public void agregar(Producto producto) {
        productos.add(producto);
    }

    @Override
    public List<Producto> obtenerTodos() {
        return List.copyOf(productos);
    }

    @Override
    public Optional<Producto> buscarPorNombreParcial(String nombreBuscado) {
        String nombreNormalizado = nombreBuscado.toLowerCase();
        return productos.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombreNormalizado))
                .findFirst();
    }
}
