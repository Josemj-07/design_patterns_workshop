package farmacia.infraestructura.persistencia.adaptadores;

import farmacia.dominio.personas.Cliente;
import farmacia.dominio.puertos.IRepositorioCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdaptadorRepositorioClienteMemoria implements IRepositorioCliente {
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
