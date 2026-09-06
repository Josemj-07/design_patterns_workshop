package farmacia.aplicacion.construccion;

import farmacia.aplicacion.casosdeuso.AcumularPuntos;
import farmacia.aplicacion.casosdeuso.AutenticarUsuario;
import farmacia.aplicacion.casosdeuso.BuscarCliente;
import farmacia.aplicacion.casosdeuso.BuscarPagable;
import farmacia.aplicacion.casosdeuso.CargarClientes;
import farmacia.aplicacion.casosdeuso.CargarPagables;
import farmacia.aplicacion.casosdeuso.CargarUsuarios;
import farmacia.aplicacion.casosdeuso.ListarClientes;
import farmacia.aplicacion.casosdeuso.ListarProductos;
import farmacia.aplicacion.casosdeuso.RegistrarVenta;
import farmacia.aplicacion.casosdeuso.VerificarAlertas;
import farmacia.aplicacion.fabricas.RegistradorFabricasIPagable;
import farmacia.aplicacion.fachada.AplicacionFarmacia;
import farmacia.dominio.puertos.IAutenticacion;
import farmacia.dominio.puertos.IFabricaPagable;
import farmacia.dominio.puertos.IFuenteTexto;
import farmacia.dominio.puertos.IRepositorioCliente;
import farmacia.dominio.puertos.IRepositorioPagable;
import farmacia.dominio.puertos.IRepositorioUsuario;
import farmacia.dominio.puertos.IRepositorioVenta;
import farmacia.infraestructura.auth.AutenticacionPorCredenciales;
import farmacia.infraestructura.notificaciones.NotificadorConsola;
import farmacia.infraestructura.persistencia.adaptadores.AdaptadorFuenteTextoArchivo;
import farmacia.infraestructura.persistencia.adaptadores.AdaptadorRepositorioClienteMemoria;
import farmacia.infraestructura.persistencia.adaptadores.AdaptadorRepositorioIPagableMemoria;
import farmacia.infraestructura.persistencia.adaptadores.AdaptadorRepositorioUsuarioMemoria;
import farmacia.infraestructura.persistencia.adaptadores.AdaptadorRepositorioVentaMemoria;

/**
 * ConcreteBuilder: unico lugar del sistema que conoce las implementaciones
 * concretas de infraestructura y el orden en que deben ensamblarse. Cada
 * paso queda nombrado (construirInfraestructura, construirRepositorios,
 * construirCasosDeUso) en vez de ser una cascada plana de "new" en Main.
 */
public class AplicacionFarmaciaBuilder implements IAplicacionFarmaciaBuilder {

    private IFuenteTexto fuenteTexto;
    private IFabricaPagable fabricaProducto;
    private NotificadorConsola notificador;

    private IRepositorioPagable repositorioProducto;
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
        fuenteTexto = new AdaptadorFuenteTextoArchivo();
        fabricaProducto = new RegistradorFabricasIPagable();
        notificador = new NotificadorConsola();
    }

    @Override
    public void construirRepositorios() {
        repositorioProducto = new AdaptadorRepositorioIPagableMemoria();
        repositorioCliente = new AdaptadorRepositorioClienteMemoria();
        repositorioUsuario = new AdaptadorRepositorioUsuarioMemoria();
        repositorioVenta = new AdaptadorRepositorioVentaMemoria();
        autenticacion = new AutenticacionPorCredenciales(repositorioUsuario);
    }

    @Override
    public void construirCasosDeUso() {
        resultado = new AplicacionFarmacia(
                new CargarPagables(repositorioProducto, fabricaProducto, fuenteTexto),
                new CargarClientes(repositorioCliente, fuenteTexto),
                new CargarUsuarios(repositorioUsuario, fuenteTexto),
                new AutenticarUsuario(autenticacion),
                new VerificarAlertas(repositorioProducto, notificador),
                new ListarProductos(repositorioProducto),
                new ListarClientes(repositorioCliente),
                new BuscarPagable(repositorioProducto),
                new BuscarCliente(repositorioCliente),
                new RegistrarVenta(repositorioProducto, repositorioVenta, notificador),
                new AcumularPuntos(repositorioCliente, notificador));
    }

    @Override
    public AplicacionFarmacia obtenerResultado() {
        return resultado;
    }
}
