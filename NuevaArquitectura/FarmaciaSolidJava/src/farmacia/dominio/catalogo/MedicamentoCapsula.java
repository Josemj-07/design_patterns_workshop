package farmacia.dominio.catalogo;

import farmacia.dominio.enums.TipoRelleno;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicamentoCapsula extends Medicamento {
    private final TipoRelleno tipoRelleno;

    public MedicamentoCapsula(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento,
            Laboratorio laboratorio,
            TipoRelleno tipoRelleno) {
        super(nombre, precio, stock, stockMinimo, fechaVencimiento, laboratorio);
        if (tipoRelleno == null) {
            throw new IllegalArgumentException("Tipo de relleno invalido");
        }
        this.tipoRelleno = tipoRelleno;
    }

    public TipoRelleno getTipoRelleno() {
        return tipoRelleno;
    }
}
