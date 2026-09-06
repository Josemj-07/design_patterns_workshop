package farmacia.aplicacion.fabricas;

import farmacia.dominio.catalogo.Cosmetico;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ConcreteCreator (Factory Method) para el formato:
 * COS;nombre;precio;stock;stockMin;fecha;marca
 */
public class CosmeticoFactory extends IPagableFactory {

    @Override
    public Cosmetico crearIPagable(String[] campos) {
        exigirCampos(campos, 7, "COS;nombre;precio;stock;stockMin;fecha;marca");
        String nombre = campos[1];
        BigDecimal precio = new BigDecimal(campos[2]);
        int stock = Integer.parseInt(campos[3]);
        int stockMinimo = Integer.parseInt(campos[4]);
        LocalDate fechaVencimiento = LocalDate.parse(campos[5]);
        String marca = campos[6];

        return new Cosmetico(nombre, precio, stock, stockMinimo, fechaVencimiento, marca);
    }
}
