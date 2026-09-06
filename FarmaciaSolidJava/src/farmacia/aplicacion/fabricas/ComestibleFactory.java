package farmacia.aplicacion.fabricas;


import farmacia.dominio.catalogo.Comestible;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ConcreteCreator (Factory Method) para el formato:
 * COM;nombre;precio;stock;stockMin;fecha;refrigerado(true|false)
 */
public class ComestibleFactory extends IPagableFactory {

    @Override
    public Comestible crearIPagable(String[] campos) {
        exigirCampos(campos, 7, "COM;nombre;precio;stock;stockMin;fecha;refrigerado(true|false)");
        String nombre = campos[1];
        BigDecimal precio = new BigDecimal(campos[2]);
        int stock = Integer.parseInt(campos[3]);
        int stockMinimo = Integer.parseInt(campos[4]);
        LocalDate fechaVencimiento = LocalDate.parse(campos[5]);
        boolean refrigerado = Boolean.parseBoolean(campos[6]);

        return new Comestible(nombre, precio, stock, stockMinimo, fechaVencimiento, refrigerado);
    }
}
