package farmacia.dominio.puertos;

import java.util.List;
import java.util.Optional;

/** Puerto para lectura de archivos de texto (bajo nivel detras de abstraccion). */
public interface IFuenteTexto {
    /**
     * @return empty si el archivo no existe
     */
    Optional<List<String>> leerLineas(String ruta);
}
