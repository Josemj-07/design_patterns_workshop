package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.personas.Cliente;
import farmacia.dominio.puertos.IFuenteTexto;
import farmacia.dominio.puertos.IRepositorioCliente;

import java.util.List;
import java.util.Optional;

public class CargarClientes extends CargadorArchivos{
    private final IRepositorioCliente repositorioCliente;

    public CargarClientes(IRepositorioCliente repositorioCliente, IFuenteTexto fuenteTexto) {
        super(fuenteTexto);
        this.repositorioCliente = repositorioCliente;
    }

    @Override
    protected void crearObjeto(String[] campos) {
        String nombre = campos[0];
        String cedula = campos[1];
        String telefono = campos[2];
        String correo = campos[3];
        Cliente cliente = new Cliente(nombre, cedula, telefono, correo);
        repositorioCliente.agregar(cliente);
    }

    @Override
    protected String mostarMensaje() {
        return "Clientes cargados";
    }

    @Override
    protected String mostarError() {
        return "Error al cargar clientes";
    }
}
