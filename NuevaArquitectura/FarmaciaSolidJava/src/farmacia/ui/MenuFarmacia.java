package farmacia.ui;

import farmacia.aplicacion.fachada.AplicacionFarmacia;
import farmacia.dominio.catalogo.Producto;
import farmacia.dominio.personas.Cliente;

import java.util.Optional;
import java.util.Scanner;

/**
 * Presentacion por consola. Solo depende de la fachada AplicacionFarmacia,
 * nunca de los casos de uso individuales ni de repositorios.
 */
public class MenuFarmacia {
    private static final String COLOR_RESET = "[0m";
    private static final String COLOR_ROJO = "[31m";
    private static final String COLOR_VERDE = "[32m";
    private static final String COLOR_AZUL = "[34m";
    private static final String COLOR_CYAN = "[36m";
    private static final String COLOR_MAGENTA = "[35m";

    private final AplicacionFarmacia aplicacionFarmacia;
    private final String rutaArchivoProductos;
    private final String rutaArchivoClientes;
    private final String rutaArchivoUsuarios;

    public MenuFarmacia(
            AplicacionFarmacia aplicacionFarmacia,
            String rutaArchivoProductos,
            String rutaArchivoClientes,
            String rutaArchivoUsuarios) {
        this.aplicacionFarmacia = aplicacionFarmacia;
        this.rutaArchivoProductos = rutaArchivoProductos;
        this.rutaArchivoClientes = rutaArchivoClientes;
        this.rutaArchivoUsuarios = rutaArchivoUsuarios;
    }

    public void ejecutar() {
        Scanner entradaConsola = new Scanner(System.in);

        System.out.println(COLOR_VERDE + "Cargando informacion del sistema...\n" + COLOR_RESET);

        System.out.println(aplicacionFarmacia.cargarProductos(rutaArchivoProductos));
        System.out.println(aplicacionFarmacia.cargarClientes(rutaArchivoClientes));
        System.out.println(aplicacionFarmacia.cargarUsuarios(rutaArchivoUsuarios));
        System.out.println();

        System.out.println(COLOR_AZUL + "=========== LOGIN ===========" + COLOR_RESET);

        System.out.print("Usuario: ");
        String nombreUsuarioIngresado = entradaConsola.nextLine();

        System.out.print("Contrasena: ");
        String contrasenaIngresada = entradaConsola.nextLine();

        boolean accesoPermitido = aplicacionFarmacia.iniciarSesion(
                nombreUsuarioIngresado, contrasenaIngresada);

        if (!accesoPermitido) {
            System.out.println(COLOR_ROJO + "\nAcceso denegado" + COLOR_RESET);
            return;
        }

        System.out.println(COLOR_VERDE + "\nLogin correcto" + COLOR_RESET);

        aplicacionFarmacia.verificarAlertas();

        int opcionMenu = 0;
        while (opcionMenu != 7) {
            System.out.println(COLOR_MAGENTA + "\n==============================");
            System.out.println("      SISTEMA FARMACIA");
            System.out.println("==============================" + COLOR_RESET);

            System.out.println("1. Ver productos");
            System.out.println("2. Ver clientes");
            System.out.println("3. Buscar producto");
            System.out.println("4. Registrar venta");
            System.out.println("5. Acumular puntos");
            System.out.println("6. Ver alertas");
            System.out.println("7. Salir");

            System.out.print("\nSeleccione opcion: ");
            try {
                opcionMenu = Integer.parseInt(entradaConsola.nextLine().trim());
            } catch (NumberFormatException errorNumero) {
                System.out.println("\nOpcion invalida");
                continue;
            }

            switch (opcionMenu) {
                case 1 -> listarProductos();
                case 2 -> listarClientes();
                case 3 -> buscarProducto(entradaConsola);
                case 4 -> registrarVenta(entradaConsola);
                case 5 -> acumularPuntos(entradaConsola);
                case 6 -> {
                    System.out.println("\nVerificando alertas...");
                    aplicacionFarmacia.verificarAlertas();
                }
                case 7 -> System.out.println(COLOR_ROJO + "\nSaliendo del sistema..." + COLOR_RESET);
                default -> System.out.println("\nOpcion invalida");
            }
        }

        System.out.println("\nFIN DEL SISTEMA");
    }

    private void listarProductos() {
        System.out.println(COLOR_CYAN + "\n===== PRODUCTOS =====" + COLOR_RESET);
        System.out.println("Nombre\t\tStock\tPrecio");
        System.out.println("-----------------------------------");
        for (Producto producto : aplicacionFarmacia.listarProductos()) {
            System.out.println(
                    producto.getNombre()
                            + "\t\t"
                            + producto.getStock()
                            + "\t"
                            + producto.getPrecio());
        }
    }

    private void listarClientes() {
        System.out.println(COLOR_VERDE + "\n===== CLIENTES =====" + COLOR_RESET);
        for (Cliente cliente : aplicacionFarmacia.listarClientes()) {
            System.out.println(cliente.getNombre() + " - Puntos: " + cliente.getPuntos());
        }
    }

    private void buscarProducto(Scanner entradaConsola) {
        System.out.print("\nIngrese nombre producto: ");
        String nombreProductoBuscado = entradaConsola.nextLine();
        Optional<Producto> productoEncontrado =
                aplicacionFarmacia.buscarProducto(nombreProductoBuscado);
        if (productoEncontrado.isPresent()) {
            Producto producto = productoEncontrado.get();
            System.out.println("\nProducto: " + producto.getNombre());
            System.out.println("Precio: " + producto.getPrecio());
            System.out.println("Stock: " + producto.getStock());
        } else {
            System.out.println("\nProducto no encontrado");
        }
    }

    private void registrarVenta(Scanner entradaConsola) {
        System.out.print("\nNombre producto: ");
        String nombreProductoVenta = entradaConsola.nextLine();

        Optional<Producto> productoExistente =
                aplicacionFarmacia.buscarProducto(nombreProductoVenta);
        if (productoExistente.isEmpty()) {
            System.out.println("\nProducto no encontrado");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidadVendida;
        try {
            cantidadVendida = Integer.parseInt(entradaConsola.nextLine().trim());
        } catch (NumberFormatException errorNumero) {
            System.out.println("\nCantidad invalida");
            return;
        }
        boolean ventaRegistrada =
                aplicacionFarmacia.registrarVenta(nombreProductoVenta, cantidadVendida);
        if (ventaRegistrada) {
            System.out.println("\nVenta registrada");
        } else {
            System.out.println("\nProducto no encontrado");
        }
    }

    private void acumularPuntos(Scanner entradaConsola) {
        System.out.print("\nNombre cliente: ");
        String nombreClienteBuscado = entradaConsola.nextLine();

        Optional<Cliente> clienteExistente =
                aplicacionFarmacia.buscarCliente(nombreClienteBuscado);
        if (clienteExistente.isEmpty()) {
            System.out.println("\nCliente no encontrado");
            return;
        }

        System.out.print("Puntos: ");
        int puntosAAcumular;
        try {
            puntosAAcumular = Integer.parseInt(entradaConsola.nextLine().trim());
        } catch (NumberFormatException errorNumero) {
            System.out.println("\nPuntos invalidos");
            return;
        }
        aplicacionFarmacia.acumularPuntos(nombreClienteBuscado, puntosAAcumular);
    }
}
