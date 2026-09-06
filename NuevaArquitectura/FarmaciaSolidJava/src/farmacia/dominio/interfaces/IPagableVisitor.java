package farmacia.dominio.interfaces;

import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.catalogo.Servicio;

/**
 * Visitor sobre la jerarquia {@link IPagable}. Concentra en un solo lugar las
 * operaciones cuyo comportamiento depende de si el pagable es un {@link Producto}
 * (bien fisico con inventario y vencimiento) o un {@link Servicio} (prestacion
 * sin inventario). Evita los {@code instanceof} + downcast repartidos por los
 * casos de uso y la presentacion.
 *
 * <p>Toda la jerarquia de {@code Producto} (Medicamento, Comestible, Cosmetico,
 * etc.) se despacha por {@link #visitarProducto(Producto)}: el eje de variacion
 * que interesa a la aplicacion es "bien con stock" vs "servicio", no cada
 * subtipo concreto de producto.
 *
 * @param <R> tipo del resultado que produce el visitante.
 */
public interface IPagableVisitor<R> {
    R visitarProducto(Producto producto);

    R visitarServicio(Servicio servicio);
}
