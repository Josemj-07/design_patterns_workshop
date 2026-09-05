package farmacia.infraestructura.fabricas;

import farmacia.dominio.catalogo.Laboratorio;
import farmacia.dominio.catalogo.MedicamentoCapsula;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.enums.TipoRelleno;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ConcreteCreator (Factory Method) para el formato original C#: 6 campos sin
 * discriminador (nombre;precio;stock;stockMin;fecha;laboratorio), compatible
 * con el AS-IS.
 */
public class MedicamentoCapsulaFormatoAntiguoFactory extends ProductoFactory {
    private final LaboratorioFlyweightFactory laboratorios;

    public MedicamentoCapsulaFormatoAntiguoFactory(LaboratorioFlyweightFactory laboratorios) {
        this.laboratorios = laboratorios;
    }

    @Override
    public Producto crearProducto(String[] campos) {
        String nombre = campos[0];
        BigDecimal precio = new BigDecimal(campos[1]);
        int stock = Integer.parseInt(campos[2]);
        int stockMinimo = Integer.parseInt(campos[3]);
        LocalDate fechaVencimiento = LocalDate.parse(campos[4]);
        String nombreLaboratorio = campos[5];

        Laboratorio laboratorio = laboratorios.buscarLaboratorio(
                nombreLaboratorio, DIRECCION_LABORATORIO_DEFECTO, TELEFONO_LABORATORIO_DEFECTO);
        return new MedicamentoCapsula(
                nombre, precio, stock, stockMinimo, fechaVencimiento, laboratorio, TipoRelleno.GEL);
    }
}
