package farmacia.aplicacion.visitantes;

import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.catalogo.Servicio;
import farmacia.dominio.interfaces.IPagableVisitor;

import java.util.Optional;

/**
 * Proyecta un pagable a {@link Producto} cuando lo es, o a vacio cuando es un
 * {@link Servicio}. Permite que la vista "Ver productos" liste solo productos
 * sin usar {@code instanceof} ni casts, y sin alterar una sola linea de salida
 * (hoy el catalogo puede contener servicios que no deben aparecer ahi).
 */
public class VisitanteComoProducto implements IPagableVisitor<Optional<Producto>> {

    @Override
    public Optional<Producto> visitarProducto(Producto producto) {
        return Optional.of(producto);
    }

    @Override
    public Optional<Producto> visitarServicio(Servicio servicio) {
        return Optional.empty();
    }
}
