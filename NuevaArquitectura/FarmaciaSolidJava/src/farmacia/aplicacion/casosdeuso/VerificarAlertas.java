package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.INotificador;
import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.dominio.catalogo.Producto;

public class VerificarAlertas {

    private final IRepositorioProducto repositorioProducto;
    private final INotificador notificador;

    public VerificarAlertas(IRepositorioProducto repositorioProducto, INotificador notificador) {
        this.repositorioProducto = repositorioProducto;
        this.notificador = notificador;
    }

    public void verificarStock() {
        for (Producto producto : repositorioProducto.obtenerTodos()) {
            if (producto.estaEnStockMinimo()) {
                notificador.stockMinimo(producto.getNombre());
            }
        }
    }

    public void verificarVencimiento() {
        for (Producto producto : repositorioProducto.obtenerTodos()) {
            if (producto.proximoAVencer()) {
                notificador.vencimiento(producto.getNombre());
            }
        }
    }
}
