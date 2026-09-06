package farmacia.dominio.puertos;

/**
 * Puerto de notificacion de ventas (DIP). Segregado (ISP): solo lo consume el
 * caso de uso de registro de venta.
 */
public interface INotificadorVentas {
    void ventaRegistrada();
}
