package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.puertos.IFuenteTexto;

import java.util.List;
import java.util.Optional;

public abstract class CargadorArchivos {
    private final IFuenteTexto fuenteTexto;

    protected CargadorArchivos(IFuenteTexto fuenteTexto) {
        this.fuenteTexto = fuenteTexto;
    }

    public String ejecutar(String rutaArchivo) {
        try {
            Optional<List<String>> lineasDelArchivo = fuenteTexto.leerLineas(rutaArchivo);
            if (lineasDelArchivo.isEmpty()) {
                return "Archivo no encontrado";
            }

            for(String lineaCruda : lineasDelArchivo.get()) {
                if(lineaCruda == null | lineaCruda.isBlank()) {
                    continue;
                }
                String lineaNormalizada = lineaCruda.replace("\uFEFF", "").trim();
                String[] campos = lineaNormalizada.split(";");
                crearObjeto(campos);
            }
            return mostarMensaje();
        }
        catch (Exception errorCarga) {
            return errorCarga.getMessage() == null
                    ? mostarError()
                    : errorCarga.getMessage();
        }
    }

    protected abstract void crearObjeto(String[] campos);
    protected abstract String mostarMensaje();
    protected abstract String mostarError();
}
