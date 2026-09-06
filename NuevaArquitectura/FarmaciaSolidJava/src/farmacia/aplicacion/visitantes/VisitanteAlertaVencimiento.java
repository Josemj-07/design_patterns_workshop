package farmacia.aplicacion.visitantes;

import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.catalogo.Servicio;
import farmacia.dominio.interfaces.IPagableVisitor;
import farmacia.dominio.puertos.INotificadorInventario;

/**
 * Notifica proximidad de vencimiento para los pagables que caducan. Un
 * {@link Servicio} nunca genera esta alerta.
 */
public class VisitanteAlertaVencimiento implements IPagableVisitor<Void> {
    private final INotificadorInventario notificador;

    public VisitanteAlertaVencimiento(INotificadorInventario notificador) {
        this.notificador = notificador;
    }

    @Override
    public Void visitarProducto(Producto producto) {
        if (producto.proximoAVencer()) {
            notificador.vencimiento(producto.getNombre());
        }
        return null;
    }

    @Override
    public Void visitarServicio(Servicio servicio) {
        return null;
    }
}
