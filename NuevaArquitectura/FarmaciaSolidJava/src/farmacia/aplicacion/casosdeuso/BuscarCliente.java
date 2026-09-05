package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IRepositorioCliente;
import farmacia.dominio.personas.Cliente;

import java.util.Optional;

public class BuscarCliente {
    private final IRepositorioCliente repositorioCliente;

    public BuscarCliente(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public Optional<Cliente> ejecutar(String nombreParcialCliente) {
        return repositorioCliente.buscarPorNombreParcial(nombreParcialCliente);
    }
}
