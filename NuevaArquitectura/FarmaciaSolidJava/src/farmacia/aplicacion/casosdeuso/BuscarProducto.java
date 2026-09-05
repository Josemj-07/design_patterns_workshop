package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.dominio.catalogo.Producto;

import java.util.Optional;

public class BuscarProducto {
    private final IRepositorioProducto repositorioProducto;

    public BuscarProducto(IRepositorioProducto repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    public Optional<Producto> ejecutar(String nombreParcialProducto) {
        return repositorioProducto.buscarPorNombreParcial(nombreParcialProducto);
    }
}
