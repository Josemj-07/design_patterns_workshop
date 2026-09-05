package farmacia.dominio.catalogo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** SC-1: bien fisico no farmaceutico. Hereda de Producto, no de Medicamento. */
public class Cosmetico extends Producto {
    private final String marca;

    public Cosmetico(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento,
            String marca) {
        super(nombre, precio, stock, stockMinimo, fechaVencimiento);
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no debe estar vacia");
        }
        this.marca = marca.trim();
    }

    public String getMarca() {
        return marca;
    }
}
