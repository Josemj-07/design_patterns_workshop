package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.visitantes.VisitanteAlertaStock;
import farmacia.aplicacion.visitantes.VisitanteAlertaVencimiento;
import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.interfaces.IPagableVisitor;
import farmacia.dominio.puertos.INotificadorInventario;
import farmacia.dominio.puertos.IRepositorioPagable;

public class VerificarAlertas {

    private final IRepositorioPagable repositorioProducto;
    private final INotificadorInventario notificador;

    public VerificarAlertas(IRepositorioPagable repositorioProducto, INotificadorInventario notificador) {
        this.repositorioProducto = repositorioProducto;
        this.notificador = notificador;
    }

    public void verificarStock() {
        IPagableVisitor<Void> visitante = new VisitanteAlertaStock(notificador);
        for (IPagable pagable : repositorioProducto.obtenerTodos()) {
            pagable.aceptar(visitante);
        }
    }

    public void verificarVencimiento() {
        IPagableVisitor<Void> visitante = new VisitanteAlertaVencimiento(notificador);
        for (IPagable pagable : repositorioProducto.obtenerTodos()) {
            pagable.aceptar(visitante);
        }
    }
}
