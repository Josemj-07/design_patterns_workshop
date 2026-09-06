package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.visitantes.VisitanteAfectacionVenta;
import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.inventario.Venta;
import farmacia.dominio.puertos.INotificadorVentas;
import farmacia.dominio.puertos.IRepositorioPagable;
import farmacia.dominio.puertos.IRepositorioVenta;

import java.time.LocalDateTime;
import java.util.Optional;

public class RegistrarVenta {
    private final IRepositorioPagable repositorioProducto;
    private final IRepositorioVenta repositorioVenta;
    private final INotificadorVentas notificador;

    public RegistrarVenta(
            IRepositorioPagable repositorioProducto,
            IRepositorioVenta repositorioVenta,
            INotificadorVentas notificador) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioVenta = repositorioVenta;
        this.notificador = notificador;
    }

    /**
     * @return true si la venta se registro; false si no se encontro el pagable.
     */
    public boolean ejecutar(String nombreParcialProducto, int cantidadVendida) {
        Optional<IPagable> productoEncontrado =
                repositorioProducto.buscarPorNombreParcial(nombreParcialProducto);
        if (productoEncontrado.isEmpty()) {
            return false;
        }
        IPagable pagableAVender = productoEncontrado.get();
        // Visitor: Producto descuenta stock (semantica AS-IS); Servicio no hace nada.
        pagableAVender.aceptar(new VisitanteAfectacionVenta(cantidadVendida));
        Venta venta = new Venta(LocalDateTime.now(), cantidadVendida, pagableAVender);
        repositorioVenta.registrar(venta);
        notificador.ventaRegistrada();
        return true;
    }
}
