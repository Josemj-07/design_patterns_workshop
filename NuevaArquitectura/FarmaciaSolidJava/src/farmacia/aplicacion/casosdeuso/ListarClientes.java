package farmacia.aplicacion.casosdeuso;

import farmacia.aplicacion.puertos.IRepositorioCliente;
import farmacia.dominio.personas.Cliente;

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
