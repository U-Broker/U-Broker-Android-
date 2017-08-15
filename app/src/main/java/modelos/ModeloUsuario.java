package modelos;

/**
 * Created by andresrodriguez on 10/08/17.
 */

public class ModeloUsuario {

    private String idFirebase;
    private String tokenFCM;
    private String nombres;
    private String apellidos;
    private String correo;
    private String contraseña;
    private String nombreCompleto;

    public ModeloUsuario(){}

    public ModeloUsuario(String idFirebase, String tokenFCM, String nombres, String apellidos, String correo, String contraseña) {
        this.idFirebase = idFirebase;
        this.tokenFCM = tokenFCM;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contraseña = contraseña;
        this.nombreCompleto = nombres + " " + apellidos;
    }



    public String[] formatearNombre(String nombreCompleto) {
        String[] respuesta = new String[2];
        String[] auxiliar = nombreCompleto.split(" ");
        switch (auxiliar.length) {
            case 2:
                respuesta[0] = auxiliar[0];
                respuesta[1] = auxiliar[1];
                break;
            case 3:
                respuesta[0] = auxiliar[0];
                respuesta[1] = auxiliar[1] + " " + auxiliar[2];
                break;
            case 4:
                respuesta[0] = auxiliar[0] + " " + auxiliar[1];
                respuesta[1] = auxiliar[2] + " " + auxiliar[3];
                break;
            default:
                for (int i = 0; i < auxiliar.length; i++) {
                    if (auxiliar.length - i <= 2) {
                        respuesta[1] += auxiliar[i];
                    } else {
                        respuesta[0] += auxiliar[i];
                    }
                }
                break;

        }
        return respuesta;
    }








    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
