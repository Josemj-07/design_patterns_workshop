package farmacia.dominio.personas;

public class Cliente extends Persona {
    private int puntos;

    public Cliente(String nombre, String cedula, String telefono, String correo) {
        super(nombre, cedula, telefono, correo);
        this.puntos = 0;
    }

    public int getPuntos() {
        return puntos;
    }

    /**
     * Misma semantica que el AS-IS: suma el valor recibido (sin filtrar signo).
     */
    public void acumularPuntos(int puntosASumar) {
        this.puntos += puntosASumar;
    }
}
