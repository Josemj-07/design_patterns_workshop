package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.personas.Cliente;
import farmacia.dominio.puertos.INotificadorFidelizacion;
import farmacia.dominio.puertos.IRepositorioCliente;

import java.util.Optional;

public class AcumularPuntos {
    private final IRepositorioCliente repositorioCliente;
    private final INotificadorFidelizacion notificador;

    public AcumularPuntos(IRepositorioCliente repositorioCliente, INotificadorFidelizacion notificador) {
        this.repositorioCliente = repositorioCliente;
        this.notificador = notificador;
    }

    /**
     * @return true si el cliente existe y se acumularon puntos.
     */
    public boolean ejecutar(String nombreParcialCliente, int puntosAAcumular) {
        Optional<Cliente> clienteEncontrado =
                repositorioCliente.buscarPorNombreParcial(nombreParcialCliente);
        if (clienteEncontrado.isEmpty()) {
            return false;
        }
        Cliente cliente = clienteEncontrado.get();
        cliente.acumularPuntos(puntosAAcumular);
        notificador.puntosAcumulados(cliente.getNombre(), puntosAAcumular);
        return true;
    }
}
