package farmacia.dominio.puertos;

import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.catalogo.Producto;

import java.util.List;
import java.util.Optional;

public interface IRepositorioPagable {
    void agregar(IPagable producto);

    List<IPagable> obtenerTodos();

    Optional<IPagable> buscarPorNombreParcial(String nombre);
}
