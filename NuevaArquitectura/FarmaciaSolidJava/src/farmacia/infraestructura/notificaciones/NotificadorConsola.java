package farmacia.infraestructura.notificaciones;

import farmacia.dominio.puertos.INotificadorFidelizacion;
import farmacia.dominio.puertos.INotificadorInventario;
import farmacia.dominio.puertos.INotificadorVentas;

/**
 * Adaptador de consola. Unico lugar con colores/ANSI (bajo nivel).
 * Mensajes identicos al AS-IS en C#.
 */
public class NotificadorConsola
        implements INotificadorInventario, INotificadorFidelizacion, INotificadorVentas {
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String COLOR_ROJO = "\u001B[31m";
    private static final String COLOR_AMARILLO = "\u001B[33m";
    private static final String COLOR_VERDE = "\u001B[32m";
    private static final String COLOR_CYAN = "\u001B[36m";

    @Override
    public void stockMinimo(String nombreProducto) {
        System.out.println(
                COLOR_ROJO + "ALERTA: stock minimo de " + nombreProducto + COLOR_RESET);
    }

    @Override
    public void vencimiento(String nombreProducto) {
        System.out.println(
                COLOR_AMARILLO
                        + "ALERTA: "
                        + nombreProducto
                        + " proximo a vencer"
                        + COLOR_RESET);
    }

    @Override
    public void puntosAcumulados(String nombreCliente, int puntosAcumulados) {
        System.out.println(
                COLOR_VERDE
                        + "Cliente "
                        + nombreCliente
                        + " acumulo "
                        + puntosAcumulados
                        + " puntos"
                        + COLOR_RESET);
    }

    @Override
    public void ventaRegistrada() {
        System.out.println(COLOR_CYAN + "Venta registrada" + COLOR_RESET);
    }
}
