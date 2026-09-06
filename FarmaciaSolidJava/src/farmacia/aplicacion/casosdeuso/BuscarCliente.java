package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.personas.Cliente;
import farmacia.dominio.puertos.IRepositorioCliente;

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
