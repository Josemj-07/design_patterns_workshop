package farmacia.dominio.catalogo;

import farmacia.dominio.enums.MaterialEnvase;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicamentoLiquido extends Medicamento {
    private final MaterialEnvase materialEnvase;
    private final int mililitros;

    public MedicamentoLiquido(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento,
            Laboratorio laboratorio,
            MaterialEnvase materialEnvase,
            int mililitros) {
        super(nombre, precio, stock, stockMinimo, fechaVencimiento, laboratorio);
        if (materialEnvase == null) {
            throw new IllegalArgumentException("Material de envase invalido");
        }
        if (mililitros <= 0) {
            throw new IllegalArgumentException("Mililitros invalidos");
        }
        this.materialEnvase = materialEnvase;
        this.mililitros = mililitros;
    }

    public MaterialEnvase getMaterialEnvase() {
        return materialEnvase;
    }

    public int getMililitros() {
        return mililitros;
    }
}
