package farmacia.dominio.inventario;

import farmacia.dominio.interfaces.*;
import farmacia.dominio.catalogo.Producto;

import java.time.LocalDateTime;

public class Venta {
    private final LocalDateTime fecha;
    private final int cantidad;
    private final IPagable pagable;

    public Venta(LocalDateTime fecha, int cantidad, IPagable pagable) {

        if (pagable == null) {
            throw new IllegalArgumentException("Producto de venta invalido");
        }
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.pagable = pagable;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public int getCantidad() {
        return cantidad;
    }
    public IPagable getPagable() {
        return pagable;
    }
}
