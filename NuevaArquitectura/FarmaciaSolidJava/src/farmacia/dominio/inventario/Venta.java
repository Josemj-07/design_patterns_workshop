package farmacia.dominio.inventario;

import farmacia.dominio.catalogo.Producto;

import java.time.LocalDateTime;

public class Venta {
    private final LocalDateTime fecha;
    private final int cantidad;
    private final Producto producto;

    public Venta(LocalDateTime fecha, int cantidad, Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException("Producto de venta invalido");
        }
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.producto = producto;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public int getCantidad() {
        return cantidad;
    }
    public Producto getProducto() {
        return producto;
    }
}
