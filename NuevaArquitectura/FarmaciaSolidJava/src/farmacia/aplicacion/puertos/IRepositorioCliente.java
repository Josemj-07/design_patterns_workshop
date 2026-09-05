package farmacia.aplicacion.puertos;

import farmacia.dominio.personas.Cliente;

import java.util.List;
import java.util.Optional;

public interface IRepositorioCliente {
    void agregar(Cliente cliente);

    List<Cliente> obtenerTodos();

    Optional<Cliente> buscarPorNombreParcial(String nombre);
}
