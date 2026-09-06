package farmacia.infraestructura.persistencia.adaptadores;

import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.puertos.IRepositorioPagable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdaptadorRepositorioIPagableMemoria implements IRepositorioPagable {
    private final List<IPagable> pagables = new ArrayList<>();

    @Override
    public void agregar(IPagable pagable) {
        pagables.add(pagable);
    }

    @Override
    public List<IPagable> obtenerTodos() {
        return List.copyOf(pagables);
    }

    @Override
    public Optional<IPagable> buscarPorNombreParcial(String nombreBuscado) {
        String nombreNormalizado = nombreBuscado.toLowerCase();
        return pagables.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombreNormalizado))
                .findFirst();
    }
}
