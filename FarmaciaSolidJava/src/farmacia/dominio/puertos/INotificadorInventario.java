package farmacia.dominio.puertos;

/**
 * Puerto de notificacion de inventario (DIP). Segregado (ISP): solo lo consumen
 * los casos de uso que vigilan stock y vencimiento.
 */
public interface INotificadorInventario {
    void stockMinimo(String nombreProducto);

    void vencimiento(String nombreProducto);
}
