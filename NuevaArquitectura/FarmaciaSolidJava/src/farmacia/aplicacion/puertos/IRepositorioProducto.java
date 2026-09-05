package farmacia.aplicacion.puertos;

import farmacia.dominio.catalogo.Producto;

import java.util.List;
import java.util.Optional;

public interface IRepositorioProducto {
    void agregar(Producto producto);

    List<Producto> obtenerTodos();

    Optional<Producto> buscarPorNombreParcial(String nombre);
}
