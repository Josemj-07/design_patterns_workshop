package farmacia.infraestructura.persistencia.adaptadores;

import farmacia.dominio.inventario.Venta;
import farmacia.dominio.puertos.IRepositorioVenta;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRepositorioVentaMemoria implements IRepositorioVenta {
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
