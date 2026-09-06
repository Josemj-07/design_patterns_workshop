package farmacia.dominio.interfaces;

import java.math.BigDecimal;

/**
 * Algo que la farmacia puede cobrar: hoy un {@link farmacia.dominio.catalogo.Producto}
 * o un {@link farmacia.dominio.catalogo.Servicio}.
 *
 * <p>La interfaz se mantiene minima (precio y nombre). Todo lo que varia segun
 * el tipo concreto (efecto de una venta sobre el inventario, alertas, proyeccion
 * a producto para el catalogo) se resuelve con {@link IPagableVisitor} via
 * {@link #aceptar(IPagableVisitor)}.
 */
public interface IPagable {
    BigDecimal getPrecio();

    String getNombre();

    <R> R aceptar(IPagableVisitor<R> visitante);
}
