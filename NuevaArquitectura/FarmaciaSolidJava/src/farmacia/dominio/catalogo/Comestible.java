package farmacia.dominio.catalogo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** SC-1: bien fisico no farmaceutico. Hereda de Producto, no de Medicamento. */
public class Comestible extends Producto {
    private final boolean refrigerado;

    public Comestible(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento,
            boolean refrigerado) {
        super(nombre, precio, stock, stockMinimo, fechaVencimiento);
        this.refrigerado = refrigerado;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }
}
