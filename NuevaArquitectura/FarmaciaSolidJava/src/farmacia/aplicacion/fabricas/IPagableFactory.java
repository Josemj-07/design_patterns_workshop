package farmacia.aplicacion.fabricas;

import farmacia.dominio.interfaces.IPagable;

/**
 * Factory Method (Creator): cada tipo de pagable define su propia forma de
 * construirse a partir de los campos de una linea de texto, sin que
 * RegistradorFabricasIPagable conozca el detalle de parseo de cada tipo.
 * Agregar un tipo nuevo = una subclase nueva, no una rama mas en un switch
 * compartido.
 */
public abstract class IPagableFactory {
    protected static final String DIRECCION_LABORATORIO_DEFECTO = "Medellin";
    protected static final String TELEFONO_LABORATORIO_DEFECTO = "4444444";

    public abstract IPagable crearIPagable(String[] campos);

    /**
     * Falla temprano con un mensaje util cuando la linea no trae los campos
     * minimos que el formato de este tipo requiere (en vez de un
     * ArrayIndexOutOfBoundsException opaco mas adelante).
     */
    protected static void exigirCampos(String[] campos, int minimo, String formatoEsperado) {
        if (campos == null || campos.length < minimo) {
            throw new IllegalArgumentException(
                    "Linea invalida, formato esperado: " + formatoEsperado);
        }
    }
}
