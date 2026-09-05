package farmacia.aplicacion.puertos;

import farmacia.dominio.inventario.Venta;

import java.util.List;

public interface IRepositorioVenta {
    void registrar(Venta venta);

    List<Venta> obtenerTodos();
}
