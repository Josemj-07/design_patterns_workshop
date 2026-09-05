package farmacia.infraestructura.fabricas;

import farmacia.dominio.catalogo.Laboratorio;
import farmacia.dominio.catalogo.MedicamentoLiquido;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.enums.MaterialEnvase;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ConcreteCreator (Factory Method) para el formato:
 * MED_LIQ;nombre;precio;stock;stockMin;fecha;laboratorio;envase;ml
 */
public class MedicamentoLiquidoFactory extends ProductoFactory {
    private final LaboratorioFlyweightFactory laboratorios;

    public MedicamentoLiquidoFactory(LaboratorioFlyweightFactory laboratorios) {
        this.laboratorios = laboratorios;
    }

    @Override
    public Producto crearProducto(String[] campos) {
        String nombre = campos[1];
        BigDecimal precio = new BigDecimal(campos[2]);
        int stock = Integer.parseInt(campos[3]);
        int stockMinimo = Integer.parseInt(campos[4]);
        LocalDate fechaVencimiento = LocalDate.parse(campos[5]);
        String nombreLaboratorio = campos[6];
        MaterialEnvase materialEnvase = MaterialEnvase.valueOf(campos[7].trim().toUpperCase());
        int mililitros = Integer.parseInt(campos[8]);

        Laboratorio laboratorio = laboratorios.buscarLaboratorio(
                nombreLaboratorio, DIRECCION_LABORATORIO_DEFECTO, TELEFONO_LABORATORIO_DEFECTO);
        return new MedicamentoLiquido(
                nombre, precio, stock, stockMinimo, fechaVencimiento, laboratorio,
                materialEnvase, mililitros);
    }
}
