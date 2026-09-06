package farmacia;

import farmacia.aplicacion.construccion.AplicacionFarmaciaBuilder;
import farmacia.aplicacion.construccion.EnsambladorAplicacion;
import farmacia.aplicacion.construccion.IAplicacionFarmaciaBuilder;
import farmacia.aplicacion.fachada.AplicacionFarmacia;
import farmacia.ui.MenuFarmacia;

import java.nio.file.Path;

/**
 * Composition root: elige el Builder y el Director, y conecta el resultado
 * (la fachada AplicacionFarmacia) con la presentacion. Ya no conoce el orden
 * interno de ensamblado (vive en EnsambladorAplicacion + AplicacionFarmaciaBuilder)
 * ni el detalle de cada caso de uso (vive detras de la fachada).
 */
public class Main {
    public static void main(String[] argumentosConsola) {
        Path carpetaDatos = resolverCarpetaDatos();

        IAplicacionFarmaciaBuilder constructor = new AplicacionFarmaciaBuilder();
        EnsambladorAplicacion ensamblador = new EnsambladorAplicacion();
        AplicacionFarmacia aplicacionFarmacia = ensamblador.ensamblarAplicacionCompleta(constructor);

        MenuFarmacia menuFarmacia = new MenuFarmacia(
                aplicacionFarmacia,
                carpetaDatos.resolve("pagables.txt").toString(),
                carpetaDatos.resolve("clientes.txt").toString(),
                carpetaDatos.resolve("usuarios.txt").toString());

        menuFarmacia.ejecutar();
    }

    private static Path resolverCarpetaDatos() {
        Path carpetaDatosActual = Path.of("datos");
        if (carpetaDatosActual.toFile().exists()) {
            return carpetaDatosActual.toAbsolutePath();
        }
        Path carpetaDatosDesdeRaizSolucion = Path.of("FarmaciaSolidJava", "datos");
        if (carpetaDatosDesdeRaizSolucion.toFile().exists()) {
            return carpetaDatosDesdeRaizSolucion.toAbsolutePath();
        }
        Path carpetaDatosDesde03Src = Path.of("03-src", "FarmaciaSolidJava", "datos");
        if (carpetaDatosDesde03Src.toFile().exists()) {
            return carpetaDatosDesde03Src.toAbsolutePath();
        }
        // Fallback: walk upward from current working dir and try to locate the datos folder under 03-src/FarmaciaSolidJava
        Path cwd = Path.of("").toAbsolutePath();
        for (Path p = cwd; p != null; p = p.getParent()) {
            Path candidato = p.resolve("03-src").resolve("FarmaciaSolidJava").resolve("datos");
            if (candidato.toFile().exists()) {
                return candidato;
            }
        }
        return carpetaDatosActual.toAbsolutePath();
    }
}
