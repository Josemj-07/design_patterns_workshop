package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.puertos.IRepositorioPagable;

import java.util.Optional;

public class BuscarPagable {
    private final IRepositorioPagable repositorioProducto;

    public BuscarPagable(IRepositorioPagable repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    public Optional<IPagable> ejecutar(String nombreParcialProducto) {
        return repositorioProducto.buscarPorNombreParcial(nombreParcialProducto);
    }
}
