package farmacia.aplicacion.fachada;

import farmacia.aplicacion.casosdeuso.AcumularPuntos;
import farmacia.aplicacion.casosdeuso.AutenticarUsuario;
import farmacia.aplicacion.casosdeuso.BuscarCliente;
import farmacia.aplicacion.casosdeuso.BuscarProducto;
import farmacia.aplicacion.casosdeuso.CargarClientes;
import farmacia.aplicacion.casosdeuso.CargarProductos;
import farmacia.aplicacion.casosdeuso.CargarUsuarios;
import farmacia.aplicacion.casosdeuso.ListarClientes;
import farmacia.aplicacion.casosdeuso.ListarProductos;
import farmacia.aplicacion.casosdeuso.RegistrarVenta;
import farmacia.aplicacion.casosdeuso.VerificarAlertas;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.personas.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Facade: unico punto que conoce los 11 casos de uso de la aplicacion.
 * La capa de presentacion (MenuFarmacia) solo depende de esta clase, nunca
 * de los casos de uso individuales. Cada metodo aqui SOLO delega, no decide:
 * las reglas de negocio siguen viviendo en los casos de uso, para no repetir
 * el error tipico de una fachada que termina absorbiendo logica de negocio.
 */
public class AplicacionFarmacia {

    private final CargarProductos casoUsoCargarProductos;
    private final CargarClientes casoUsoCargarClientes;
    private final CargarUsuarios casoUsoCargarUsuarios;
    private final AutenticarUsuario casoUsoAutenticarUsuario;
    private final VerificarAlertas casoUsoVerificarAlertas;
    private final ListarProductos casoUsoListarProductos;
    private final ListarClientes casoUsoListarClientes;
    private final BuscarProducto casoUsoBuscarProducto;
    private final BuscarCliente casoUsoBuscarCliente;
    private final RegistrarVenta casoUsoRegistrarVenta;
    private final AcumularPuntos casoUsoAcumularPuntos;

    public AplicacionFarmacia(
            CargarProductos casoUsoCargarProductos,
            CargarClientes casoUsoCargarClientes,
            CargarUsuarios casoUsoCargarUsuarios,
            AutenticarUsuario casoUsoAutenticarUsuario,
            VerificarAlertas casoUsoVerificarAlertas,
            ListarProductos casoUsoListarProductos,
            ListarClientes casoUsoListarClientes,
            BuscarProducto casoUsoBuscarProducto,
            BuscarCliente casoUsoBuscarCliente,
            RegistrarVenta casoUsoRegistrarVenta,
            AcumularPuntos casoUsoAcumularPuntos) {
        this.casoUsoCargarProductos = casoUsoCargarProductos;
        this.casoUsoCargarClientes = casoUsoCargarClientes;
        this.casoUsoCargarUsuarios = casoUsoCargarUsuarios;
        this.casoUsoAutenticarUsuario = casoUsoAutenticarUsuario;
        this.casoUsoVerificarAlertas = casoUsoVerificarAlertas;
        this.casoUsoListarProductos = casoUsoListarProductos;
        this.casoUsoListarClientes = casoUsoListarClientes;
        this.casoUsoBuscarProducto = casoUsoBuscarProducto;
        this.casoUsoBuscarCliente = casoUsoBuscarCliente;
        this.casoUsoRegistrarVenta = casoUsoRegistrarVenta;
        this.casoUsoAcumularPuntos = casoUsoAcumularPuntos;
    }

    public String cargarProductos(String rutaArchivo) {
        return casoUsoCargarProductos.ejecutar(rutaArchivo);
    }

    public String cargarClientes(String rutaArchivo) {
        return casoUsoCargarClientes.ejecutar(rutaArchivo);
    }

    public String cargarUsuarios(String rutaArchivo) {
        return casoUsoCargarUsuarios.ejecutar(rutaArchivo);
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        return casoUsoAutenticarUsuario.ejecutar(nombreUsuario, contrasena);
    }

    public void verificarAlertas() {
        casoUsoVerificarAlertas.verificarStock();
        casoUsoVerificarAlertas.verificarVencimiento();
    }

    public List<Producto> listarProductos() {
        return casoUsoListarProductos.ejecutar();
    }

    public List<Cliente> listarClientes() {
        return casoUsoListarClientes.ejecutar();
    }

    public Optional<Producto> buscarProducto(String nombreParcialProducto) {
        return casoUsoBuscarProducto.ejecutar(nombreParcialProducto);
    }

    public Optional<Cliente> buscarCliente(String nombreParcialCliente) {
        return casoUsoBuscarCliente.ejecutar(nombreParcialCliente);
    }

    public boolean registrarVenta(String nombreParcialProducto, int cantidadVendida) {
        return casoUsoRegistrarVenta.ejecutar(nombreParcialProducto, cantidadVendida);
    }

    public boolean acumularPuntos(String nombreParcialCliente, int puntosAAcumular) {
        return casoUsoAcumularPuntos.ejecutar(nombreParcialCliente, puntosAAcumular);
    }
}
