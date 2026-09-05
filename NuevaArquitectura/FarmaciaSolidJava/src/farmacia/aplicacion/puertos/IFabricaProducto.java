package farmacia.aplicacion.puertos;

import farmacia.dominio.catalogo.Producto;

/**
 * OCP: nuevos tipos de producto (SC-1) se agregan aqui sin reabrir el repositorio.
 */
public interface IFabricaProducto {
    Producto crearDesdeLinea(String linea);
}
