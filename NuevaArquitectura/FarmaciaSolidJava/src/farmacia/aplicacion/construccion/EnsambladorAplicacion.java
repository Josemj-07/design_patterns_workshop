package farmacia.aplicacion.construccion;

import farmacia.aplicacion.fachada.AplicacionFarmacia;

/**
 * Director: conoce el orden correcto de los pasos de construccion, pero no
 * como se construye cada pieza (eso lo sabe el Builder que recibe).
 */
public class EnsambladorAplicacion {

    public AplicacionFarmacia ensamblarAplicacionCompleta(IAplicacionFarmaciaBuilder constructor) {
        constructor.comenzar();
        constructor.construirInfraestructura();
        constructor.construirRepositorios();
        constructor.construirCasosDeUso();
        return constructor.obtenerResultado();
    }
}
