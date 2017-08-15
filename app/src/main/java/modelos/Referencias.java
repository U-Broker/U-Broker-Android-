package modelos;

import java.util.List;

/**
 * Created by andresrodriguez on 03/08/17.
 */

public class Referencias {


    private String id_referencia;
    private String nombre;
    private String apellido;
    private List<Referencias> referenciados;
    private String mensaje;



    public String getId_referencia() {
        return id_referencia;
    }

    public void setId_referencia(String id_referencia) {
        this.id_referencia = id_referencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<Referencias> getReferenciados() {
        return referenciados;
    }

    public void setReferenciados(List<Referencias> referenciados) {
        this.referenciados = referenciados;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }



    public Referencias(String id_referencia, String nombre, String apellido, List <Referencias> referenciados, String mensaje){
        this.id_referencia = id_referencia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.referenciados = referenciados;
        this.mensaje = mensaje;
    }





}
