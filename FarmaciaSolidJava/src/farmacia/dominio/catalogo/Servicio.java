package farmacia.dominio.catalogo;

import farmacia.dominio.interfaces.IPagable;
import farmacia.dominio.interfaces.IPagableVisitor;

import java.math.BigDecimal;

public class Servicio implements IPagable {

    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;

    public Servicio(String nombre, String descripcion, BigDecimal precio) {
        if(nombre == null || nombre.isBlank()) throw new IllegalArgumentException("nombre no puede estar vacio");
        if(descripcion == null || descripcion.isBlank()) throw new IllegalArgumentException("descripcion no puede estar vacio");
        if(precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("no puede haber precios menores o iguales que cero");

        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    @Override
    public BigDecimal getPrecio() {
        return this.precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public <R> R aceptar(IPagableVisitor<R> visitante) {
        return visitante.visitarServicio(this);
    }
}