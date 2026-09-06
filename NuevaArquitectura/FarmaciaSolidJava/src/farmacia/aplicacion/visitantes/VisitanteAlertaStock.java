package farmacia.aplicacion.visitantes;

import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.catalogo.Servicio;
import farmacia.dominio.interfaces.IPagableVisitor;
import farmacia.dominio.puertos.INotificadorInventario;

/**
 * Notifica stock minimo para los pagables que tienen inventario. Un
 * {@link Servicio} nunca genera esta alerta.
 *
 * <p>Sustituye el {@code filter(x -> x instanceof Producto)} de VerificarAlertas.
 */
public class VisitanteAlertaStock implements IPagableVisitor<Void> {
    private final INotificadorInventario notificador;

    public VisitanteAlertaStock(INotificadorInventario notificador) {
        this.notificador = notificador;
    }

    @Override
    public Void visitarProducto(Producto producto) {
        if (producto.estaEnStockMinimo()) {
            notificador.stockMinimo(producto.getNombre());
        }
        return null;
    }

    @Override
    public Void visitarServicio(Servicio servicio) {
        return null;
    }
}
