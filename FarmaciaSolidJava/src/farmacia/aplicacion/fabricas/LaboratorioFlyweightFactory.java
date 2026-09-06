package farmacia.aplicacion.fabricas;

import farmacia.dominio.catalogo.Laboratorio;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight Factory: comparte instancias de Laboratorio (dato intrinseco:
 * nombre, direccion, telefono) entre todos los productos que declaran el
 * mismo laboratorio, en vez de crear una instancia nueva por cada linea del
 * archivo de productos.
 */
public class LaboratorioFlyweightFactory {

    private final Map<String, Laboratorio> laboratoriosCompartidos = new HashMap<>();

    public Laboratorio buscarLaboratorio(String nombre, String direccion, String telefono) {
        String clave = nombre.trim().toLowerCase();
        return laboratoriosCompartidos.computeIfAbsent(
                clave, claveIgnorada -> new Laboratorio(nombre, direccion, telefono));
    }
}
