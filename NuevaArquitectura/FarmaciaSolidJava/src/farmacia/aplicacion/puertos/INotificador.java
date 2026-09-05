package farmacia.aplicacion.puertos;

/**
 * Puerto de notificacion (DIP). La consola vive en infraestructura.
 * Metodos segregados (ISP) segun el tipo de aviso del AS-IS.
 */
public interface INotificador {
    void stockMinimo(String nombreProducto);

    void vencimiento(String nombreProducto);

    void puntosAcumulados(String nombreCliente, int puntosAcumulados);

    void ventaRegistrada();
}
