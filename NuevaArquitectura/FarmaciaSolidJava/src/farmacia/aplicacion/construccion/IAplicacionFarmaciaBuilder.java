package farmacia.aplicacion.construccion;

import farmacia.aplicacion.fachada.AplicacionFarmacia;

/**
 * Builder: separa el "como se construye" (que infraestructura concreta y en
 * que orden se arma AplicacionFarmacia) del "que se construye" (la fachada
 * en si, que es lo unico que MenuFarmacia necesita conocer).
 */
public interface IAplicacionFarmaciaBuilder {
    void comenzar();

    void construirInfraestructura();

    void construirRepositorios();

    void construirCasosDeUso();

    AplicacionFarmacia obtenerResultado();
}
