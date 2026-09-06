package farmacia.dominio.puertos;

/**
 * Puerto de notificacion de fidelizacion (DIP). Segregado (ISP): solo lo
 * consume el caso de uso de acumulacion de puntos.
 */
public interface INotificadorFidelizacion {
    void puntosAcumulados(String nombreCliente, int puntosAcumulados);
}
