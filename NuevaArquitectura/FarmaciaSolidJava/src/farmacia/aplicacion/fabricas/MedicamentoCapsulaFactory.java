package farmacia.aplicacion.fabricas;

import farmacia.dominio.interfaces.*;
import farmacia.dominio.catalogo.Laboratorio;
import farmacia.dominio.catalogo.MedicamentoCapsula;
import farmacia.dominio.enums.TipoRelleno;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ConcreteCreator (Factory Method) para el formato:
 * MED_CAP;nombre;precio;stock;stockMin;fecha;laboratorio;relleno
 */
public class MedicamentoCapsulaFactory extends IPagableFactory {
    private final LaboratorioFlyweightFactory laboratorios;

    public MedicamentoCapsulaFactory(LaboratorioFlyweightFactory laboratorios) {
        this.laboratorios = laboratorios;
    }

    @Override
    public IPagable crearIPagable(String[] campos) {
        exigirCampos(campos, 7, "MED_CAP;nombre;precio;stock;stockMin;fecha;laboratorio;relleno");
        String nombre = campos[1];
        BigDecimal precio = new BigDecimal(campos[2]);
        int stock = Integer.parseInt(campos[3]);
        int stockMinimo = Integer.parseInt(campos[4]);
        LocalDate fechaVencimiento = LocalDate.parse(campos[5]);
        String nombreLaboratorio = campos[6];
        TipoRelleno tipoRelleno = campos.length > 7
                ? TipoRelleno.valueOf(campos[7].trim().toUpperCase())
                : TipoRelleno.GEL;

        Laboratorio laboratorio = laboratorios.buscarLaboratorio(
                nombreLaboratorio, DIRECCION_LABORATORIO_DEFECTO, TELEFONO_LABORATORIO_DEFECTO);
        return new MedicamentoCapsula(
                nombre, precio, stock, stockMinimo, fechaVencimiento, laboratorio, tipoRelleno);
    }
}
