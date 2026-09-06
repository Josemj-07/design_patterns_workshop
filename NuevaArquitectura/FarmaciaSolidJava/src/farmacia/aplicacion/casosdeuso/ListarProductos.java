package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.visitantes.VisitanteComoProducto;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.puertos.IRepositorioPagable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Vista de catalogo "Ver productos": devuelve solo los pagables que son
 * {@link Producto}. Los {@link farmacia.dominio.catalogo.Servicio} viven en el
 * mismo repositorio pero no forman parte de esta vista (se venden y se buscan,
 * no se listan aqui). El filtrado se hace con {@link VisitanteComoProducto},
 * sin {@code instanceof} ni casts.
 */
public class ListarProductos {
    private final IRepositorioPagable repositorioProducto;

    public ListarProductos(IRepositorioPagable repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    public List<Producto> ejecutar() {
        VisitanteComoProducto proyeccion = new VisitanteComoProducto();
        return repositorioProducto.obtenerTodos().stream()
                .map(pagable -> pagable.aceptar(proyeccion))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
