package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.dominio.catalogo.Producto;

import java.util.List;

public class ListarProductos {
    private final IRepositorioProducto repositorioProducto;

    public ListarProductos(IRepositorioProducto repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    public List<Producto> ejecutar() {
        return repositorioProducto.obtenerTodos();
    }
}
