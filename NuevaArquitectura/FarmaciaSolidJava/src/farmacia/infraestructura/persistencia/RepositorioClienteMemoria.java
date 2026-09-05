package farmacia.infraestructura.persistencia;

import farmacia.aplicacion.puertos.IRepositorioCliente;
import farmacia.dominio.personas.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioClienteMemoria implements IRepositorioCliente {
    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    public void agregar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return List.copyOf(clientes);
    }

    @Override
    public Optional<Cliente> buscarPorNombreParcial(String nombreBuscado) {
        String nombreNormalizado = nombreBuscado.toLowerCase();
        return clientes.stream()
                .filter(cliente -> cliente.getNombre().toLowerCase().contains(nombreNormalizado))
                .findFirst();
    }
}
