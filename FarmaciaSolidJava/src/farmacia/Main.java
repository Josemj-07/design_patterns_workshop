package farmacia;

import farmacia.aplicacion.construccion.AplicacionFarmaciaBuilder;
import farmacia.aplicacion.construccion.EnsambladorAplicacion;
import farmacia.aplicacion.construccion.IAplicacionFarmaciaBuilder;
import farmacia.aplicacion.fachada.AplicacionFarmacia;
import farmacia.ui.MenuFarmacia;

import java.nio.file.Files;
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
        String[][] candidatosRelativos = new String[][]{
                {"datos"},
                {"NuevaArquitectura", "FarmaciaSolidJava", "datos"},
                {"FarmaciaSolidJava", "datos"},
                {"03-src", "FarmaciaSolidJava", "datos"}
        };

        Path cwd = Path.of("").toAbsolutePath();
        for (Path p = cwd; p != null; p = p.getParent()) {
            for (String[] candidatoRelativo : candidatosRelativos) {
                Path candidato = p;
                for (String parte : candidatoRelativo) {
                    candidato = candidato.resolve(parte);
                }
                if (Files.isDirectory(candidato)) {
                    return candidato.toAbsolutePath();
                }
            }
        }

        return cwd.resolve("NuevaArquitectura").resolve("FarmaciaSolidJava").resolve("datos").toAbsolutePath();
    }
}
