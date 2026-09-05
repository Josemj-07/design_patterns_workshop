package farmacia.infraestructura.persistencia;

import farmacia.aplicacion.puertos.IRepositorioVenta;
import farmacia.dominio.inventario.Venta;

import java.util.ArrayList;
import java.util.List;

public class RepositorioVentaMemoria implements IRepositorioVenta {
    private final List<Venta> ventas = new ArrayList<>();

    @Override
    public void registrar(Venta venta) {
        ventas.add(venta);
    }

    @Override
    public List<Venta> obtenerTodos() {
        return List.copyOf(ventas);
    }
}
