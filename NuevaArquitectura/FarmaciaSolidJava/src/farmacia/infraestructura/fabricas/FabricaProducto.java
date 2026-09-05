package farmacia.infraestructura.fabricas;

import farmacia.aplicacion.puertos.IFabricaProducto;
import farmacia.dominio.catalogo.Producto;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Method: FabricaProducto ya no decide "como" se construye cada tipo
 * de producto, solo identifica el discriminador de la linea y delega en el
 * ProductoFactory (ConcreteCreator) registrado para ese tipo. Agregar un tipo
 * de producto nuevo = una clase ProductoFactory nueva + una linea de registro
 * aqui; no se toca la logica de parseo de los tipos existentes.
 *
 * Compatibilidad AS-IS: cualquier discriminador no registrado (incluyendo una
 * linea sin prefijo, formato original C# de 6 campos) cae en
 * MedicamentoCapsulaFormatoAntiguoFactory, igual que el "default" original.
 *
 * Formatos:
 *   nombre;precio;stock;stockMin;fecha;laboratorio
 *   MED_CAP;nombre;precio;stock;stockMin;fecha;laboratorio;relleno
 *   MED_LIQ;nombre;precio;stock;stockMin;fecha;laboratorio;envase;ml
 *   COS;nombre;precio;stock;stockMin;fecha;marca
 *   COM;nombre;precio;stock;stockMin;fecha;refrigerado(true|false)
 */
public class FabricaProducto implements IFabricaProducto {

    private final Map<String, ProductoFactory> fabricasPorDiscriminador = new HashMap<>();
    private final ProductoFactory fabricaFormatoAntiguo;

    public FabricaProducto() {
        LaboratorioFlyweightFactory laboratorios = new LaboratorioFlyweightFactory();

        fabricasPorDiscriminador.put("MED_CAP", new MedicamentoCapsulaFactory(laboratorios));
        fabricasPorDiscriminador.put("MED_LIQ", new MedicamentoLiquidoFactory(laboratorios));
        fabricasPorDiscriminador.put("COS", new CosmeticoFactory());
        fabricasPorDiscriminador.put("COM", new ComestibleFactory());

        this.fabricaFormatoAntiguo = new MedicamentoCapsulaFormatoAntiguoFactory(laboratorios);
    }

    @Override
    public Producto crearDesdeLinea(String linea) {
        String[] campos = linea.split(";");
        if (campos.length == 0) {
            throw new IllegalArgumentException("Linea de producto vacia");
        }

        String discriminadorTipo = campos[0].trim().toUpperCase();
        ProductoFactory fabrica = fabricasPorDiscriminador.get(discriminadorTipo);
        if (fabrica == null) {
            return fabricaFormatoAntiguo.crearProducto(campos);
        }
        return fabrica.crearProducto(campos);
    }
}
