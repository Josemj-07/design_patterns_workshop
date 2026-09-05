package farmacia.dominio.catalogo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Producto {
    private final String nombre;
    private final BigDecimal precio;
    private int stock;
    private final int stockMinimo;
    private final LocalDate fechaVencimiento;
    private static final int DIAS_UMBRAL_ALERTA_VENCIMIENTO = 30;

    protected Producto(
            String nombre,
            BigDecimal precio,
            int stock,
            int stockMinimo,
            LocalDate fechaVencimiento) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El producto debe tener un nombre");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio invalido");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("Stock minimo invalido");
        }
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("Fecha de vencimiento invalida");
        }

        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Misma semantica que el AS-IS: descuenta sin impedir stock negativo.
     */
    public void descontarStock(int cantidadADescontar) {
        if(cantidadADescontar <= 0) {
            throw new IllegalArgumentException("Cantidad a descontar invalida");
        }
        this.stock -= cantidadADescontar;
    }

    public boolean estaEnStockMinimo() {
        return stock <= stockMinimo;
    }

    public boolean proximoAVencer() {
        int diasRestantesParaVencer =
                (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        return DIAS_UMBRAL_ALERTA_VENCIMIENTO >= diasRestantesParaVencer;
    }
}
