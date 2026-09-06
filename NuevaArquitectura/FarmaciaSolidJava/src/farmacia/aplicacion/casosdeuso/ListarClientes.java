package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.personas.Cliente;
import farmacia.dominio.puertos.IRepositorioCliente;

import java.util.List;

public class ListarClientes {
    private final IRepositorioCliente repositorioCliente;

    public ListarClientes(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public List<Cliente> ejecutar() {
        return repositorioCliente.obtenerTodos();
    }
}
