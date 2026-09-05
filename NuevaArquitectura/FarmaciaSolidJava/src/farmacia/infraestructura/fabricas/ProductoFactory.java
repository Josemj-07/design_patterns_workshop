package farmacia.infraestructura.fabricas;

import farmacia.dominio.catalogo.Producto;

/**
 * Factory Method (Creator): cada tipo de producto define su propia forma de
 * construirse a partir de los campos de una linea de texto, sin que
 * FabricaProducto conozca el detalle de parseo de cada tipo.
 * Agregar un tipo de producto nuevo = una subclase nueva, no una rama mas
 * en un switch compartido.
 */
public abstract class ProductoFactory {
    protected static final String DIRECCION_LABORATORIO_DEFECTO = "Medellin";
    protected static final String TELEFONO_LABORATORIO_DEFECTO = "4444444";

    public abstract Producto crearProducto(String[] campos);
}
