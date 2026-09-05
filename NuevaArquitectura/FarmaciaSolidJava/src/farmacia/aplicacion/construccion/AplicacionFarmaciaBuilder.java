package farmacia.aplicacion.construccion;

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
import farmacia.aplicacion.fachada.AplicacionFarmacia;
import farmacia.aplicacion.puertos.IAutenticacion;
import farmacia.aplicacion.puertos.IFabricaProducto;
import farmacia.aplicacion.puertos.IFuenteTexto;
import farmacia.aplicacion.puertos.INotificador;
import farmacia.aplicacion.puertos.IRepositorioCliente;
import farmacia.aplicacion.puertos.IRepositorioProducto;
import farmacia.aplicacion.puertos.IRepositorioUsuario;
import farmacia.aplicacion.puertos.IRepositorioVenta;
import farmacia.infraestructura.auth.AutenticacionPorCredenciales;
import farmacia.infraestructura.fabricas.FabricaProducto;
import farmacia.infraestructura.notificaciones.NotificadorConsola;
import farmacia.infraestructura.persistencia.FuenteTextoArchivo;
import farmacia.infraestructura.persistencia.RepositorioClienteMemoria;
import farmacia.infraestructura.persistencia.RepositorioProductoMemoria;
import farmacia.infraestructura.persistencia.RepositorioUsuarioMemoria;
import farmacia.infraestructura.persistencia.RepositorioVentaMemoria;

/**
 * ConcreteBuilder: unico lugar del sistema que conoce las implementaciones
 * concretas de infraestructura y el orden en que deben ensamblarse. Cada
 * paso queda nombrado (construirInfraestructura, construirRepositorios,
 * construirCasosDeUso) en vez de ser una cascada plana de "new" en Main.
 */
public class AplicacionFarmaciaBuilder implements IAplicacionFarmaciaBuilder {

    private IFuenteTexto fuenteTexto;
    private IFabricaProducto fabricaProducto;
    private INotificador notificador;

    private IRepositorioProducto repositorioProducto;
    private IRepositorioCliente repositorioCliente;
    private IRepositorioUsuario repositorioUsuario;
    private IRepositorioVenta repositorioVenta;
    private IAutenticacion autenticacion;

    private AplicacionFarmacia resultado;

    @Override
    public void comenzar() {
        fuenteTexto = null;
        fabricaProducto = null;
        notificador = null;
        repositorioProducto = null;
        repositorioCliente = null;
        repositorioUsuario = null;
        repositorioVenta = null;
        autenticacion = null;
        resultado = null;
    }

    @Override
    public void construirInfraestructura() {
        fuenteTexto = new FuenteTextoArchivo();
        fabricaProducto = new FabricaProducto();
        notificador = new NotificadorConsola();
    }

    @Override
    public void construirRepositorios() {
        repositorioProducto = new RepositorioProductoMemoria();
        repositorioCliente = new RepositorioClienteMemoria();
        repositorioUsuario = new RepositorioUsuarioMemoria();
        repositorioVenta = new RepositorioVentaMemoria();
        autenticacion = new AutenticacionPorCredenciales(repositorioUsuario);
    }

    @Override
    public void construirCasosDeUso() {
        resultado = new AplicacionFarmacia(
                new CargarProductos(repositorioProducto, fabricaProducto, fuenteTexto),
                new CargarClientes(repositorioCliente, fuenteTexto),
                new CargarUsuarios(repositorioUsuario, fuenteTexto),
                new AutenticarUsuario(autenticacion),
                new VerificarAlertas(repositorioProducto, notificador),
                new ListarProductos(repositorioProducto),
                new ListarClientes(repositorioCliente),
                new BuscarProducto(repositorioProducto),
                new BuscarCliente(repositorioCliente),
                new RegistrarVenta(repositorioProducto, repositorioVenta, notificador),
                new AcumularPuntos(repositorioCliente, notificador));
    }

    @Override
    public AplicacionFarmacia obtenerResultado() {
        return resultado;
    }
}
