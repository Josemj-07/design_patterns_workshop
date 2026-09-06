package farmacia.aplicacion.fabricas;

import farmacia.dominio.catalogo.Servicio;

import java.math.BigDecimal;

/**
 * ConcreteCreator (Factory Method) para el formato:
 * SER;nombre;precio;descripcion
 */
public class ServicioFactory extends IPagableFactory {
    @Override
    public Servicio crearIPagable(String[] campos) {
        exigirCampos(campos, 4, "SER;nombre;precio;descripcion");
        String nombre = campos[1];
        BigDecimal precio = new BigDecimal(campos[2]);
        String descripcion = campos[3];

        return new Servicio(nombre, descripcion, precio);
    }
}
