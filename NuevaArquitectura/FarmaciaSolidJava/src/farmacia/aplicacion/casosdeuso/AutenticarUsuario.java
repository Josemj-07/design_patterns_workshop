package farmacia.aplicacion.casosdeuso;

import farmacia.dominio.puertos.IAutenticacion;

public class AutenticarUsuario {
    private final IAutenticacion autenticacion;

    public AutenticarUsuario(IAutenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public boolean ejecutar(String nombreUsuario, String contrasena) {
        return autenticacion.login(nombreUsuario, contrasena);
    }
}
