package farmacia.dominio.catalogo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento extends Producto {
    private final Laboratorio laboratorio;

    public Medicamento(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento,
            Laboratorio laboratorio) {
        super(nombre, precio, stock, stockMinimo, fechaVencimiento);
        if (laboratorio == null) {
            throw new IllegalArgumentException("Laboratorio obligatorio para medicamento");
        }
        this.laboratorio = laboratorio;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }
}
