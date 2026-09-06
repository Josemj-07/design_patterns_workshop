package farmacia.dominio.puertos;

import farmacia.dominio.interfaces.*;

/**
 * OCP: nuevos tipos de producto (SC-1) se agregan aqui sin reabrir el repositorio.
 */
public interface IFabricaPagable {
    IPagable crearDesdeLinea(String linea);
}
