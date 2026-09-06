package farmacia.aplicacion.visitantes;

import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.catalogo.Servicio;
import farmacia.dominio.interfaces.IPagableVisitor;

/**
 * Aplica el efecto de una venta sobre el inventario segun el tipo de pagable:
 * un {@link Producto} descuenta stock (misma semantica que el AS-IS); un
 * {@link Servicio} no tiene inventario, asi que no hace nada.
 *
 * <p>Reemplaza el {@code (Producto) ...} incondicional de RegistrarVenta, que
 * hacia imposible vender un servicio (ClassCastException).
 */
public class VisitanteAfectacionVenta implements IPagableVisitor<Void> {
    private final int cantidadVendida;

    public VisitanteAfectacionVenta(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    @Override
    public Void visitarProducto(Producto producto) {
        producto.descontarStock(cantidadVendida);
        return null;
    }

    @Override
    public Void visitarServicio(Servicio servicio) {
        // Un servicio no maneja inventario: la venta no afecta ningun stock.
        return null;
    }
}
