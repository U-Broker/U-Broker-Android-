package modelos;

/**
 * Created by andresrodriguez on 28/06/17.
 */

public class ModeloCliente {

    private int idCliente, porcentajeAvance, imagen, idProducto;
    private String nombres;



    private String nombreProducto;
    private String apellidos;
    private String celular;
    private String correo;
    private String idReferencia;
    private String comentarios;


    private String nombreEtapa;


    public ModeloCliente(int idCliente, int idProducto, String nombres, String apellidos, String celular, String correo, String idReferencia) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;

        this.idReferencia = idReferencia;
    }


    public ModeloCliente(int id, int img, String nombreProducto, String nombres, String apellidos, String celular, String correo, String idReferencia, String etapa, int porcentaje, String comentario) {
        this.idCliente = id;
        this.imagen = img;
        this.nombreEtapa = nombreProducto;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
        this.idReferencia = idReferencia;
        this.comentarios = comentario;
        this.porcentajeAvance = porcentaje;
        this.nombreEtapa = etapa;
    }


    public ModeloCliente(int idCliente, int idProducto, String nombres, String apellidos, String celular, String correo) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
    }

    public String getNombreEtapa() {return nombreEtapa;}

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public String getCorreo() {
        return correo;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public int getPorcentajeAvance() {return porcentajeAvance;}

    public String getComentarios() {return comentarios;}

    public int getImagen() {
        return imagen;
    }
    public String getNombreProducto() {

        return nombreProducto;
    }
}
