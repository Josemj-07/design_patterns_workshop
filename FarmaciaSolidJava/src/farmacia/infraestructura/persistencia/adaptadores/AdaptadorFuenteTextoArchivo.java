package farmacia.infraestructura.persistencia.adaptadores;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import farmacia.dominio.puertos.IFuenteTexto;

public class AdaptadorFuenteTextoArchivo implements IFuenteTexto {
    @Override
    public Optional<List<String>> leerLineas(String rutaArchivo) {
        Path archivo = Path.of(rutaArchivo);
        if (!Files.exists(archivo)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.readAllLines(archivo, StandardCharsets.UTF_8));
        } catch (IOException errorLectura) {
            throw new IllegalStateException(errorLectura.getMessage(), errorLectura);
        }
    }
}
