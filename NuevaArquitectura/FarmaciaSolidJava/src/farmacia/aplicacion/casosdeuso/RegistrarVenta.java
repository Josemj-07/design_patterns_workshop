package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.INotificador;
import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.aplicacion.puertos.IRepositorioVenta;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.inventario.Venta;

import java.time.LocalDateTime;
import java.util.Optional;

public class RegistrarVenta {
    private final IRepositorioProducto repositorioProducto;
    private final IRepositorioVenta repositorioVenta;
    private final INotificador notificador;

    public RegistrarVenta(
            IRepositorioProducto repositorioProducto,
            IRepositorioVenta repositorioVenta,
            INotificador notificador) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioVenta = repositorioVenta;
        this.notificador = notificador;
    }

    /**
     * @return true si la venta se registro; false si no se encontro el producto.
     */
    public boolean ejecutar(String nombreParcialProducto, int cantidadVendida) {
        Optional<Producto> productoEncontrado =
                repositorioProducto.buscarPorNombreParcial(nombreParcialProducto);
        if (productoEncontrado.isEmpty()) {
            return false;
        }
        Producto productoAVender = productoEncontrado.get();
        productoAVender.descontarStock(cantidadVendida);
        Venta venta = new Venta(LocalDateTime.now(), cantidadVendida, productoAVender);
        repositorioVenta.registrar(venta);
        notificador.ventaRegistrada();
        return true;
    }
}
