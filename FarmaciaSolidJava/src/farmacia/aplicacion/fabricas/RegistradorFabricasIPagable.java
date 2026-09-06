package farmacia.aplicacion.fabricas;

import farmacia.dominio.interfaces.*;
import farmacia.dominio.puertos.IFabricaPagable;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Method: RegistradorFabricasIPagable ya no decide "como" se construye cada tipo
 * de producto, solo identifica el discriminador de la linea y delega en el
 * IPagableFactory (ConcreteCreator) registrado para ese tipo. Agregar un tipo
 * de producto nuevo = una clase IPagableFactory nueva + una linea de registro
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
 *   SER;nombre;precio;descripcion
 */
public class RegistradorFabricasIPagable implements IFabricaPagable {

    private final Map<String, IPagableFactory> fabricasPorDiscriminador = new HashMap<>();
    private final IPagableFactory fabricaFormatoAntiguo;

    public RegistradorFabricasIPagable() {
        LaboratorioFlyweightFactory laboratorios = new LaboratorioFlyweightFactory();

        fabricasPorDiscriminador.put("MED_CAP", new MedicamentoCapsulaFactory(laboratorios));
        fabricasPorDiscriminador.put("MED_LIQ", new MedicamentoLiquidoFactory(laboratorios));
        fabricasPorDiscriminador.put("COS", new CosmeticoFactory());
        fabricasPorDiscriminador.put("COM", new ComestibleFactory());
        fabricasPorDiscriminador.put("SER", new ServicioFactory());
        this.fabricaFormatoAntiguo = new MedicamentoCapsulaFormatoAntiguoFactory(laboratorios);
    }

    @Override
    public IPagable crearDesdeLinea(String linea) {

        if(linea == null || linea.isBlank()) throw new IllegalArgumentException("linea no debe estar vacío"); //garantiza que la entrada contenga datos
        
        String[] campos = linea.split(";");
        if (campos.length == 0) {
            throw new IllegalArgumentException("Linea de producto vacia");
        }

        String discriminadorTipo = campos[0].trim().toUpperCase();
        IPagableFactory fabrica = fabricasPorDiscriminador.get(discriminadorTipo);
        if (fabrica == null) {
            return fabricaFormatoAntiguo.crearIPagable(campos);
        }
        return fabrica.crearIPagable(campos);
    }
}
