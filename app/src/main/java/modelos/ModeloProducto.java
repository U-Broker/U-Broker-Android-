package modelos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andresrodriguez on 21/06/17.
 */

public class ModeloProducto {

    private String nombre;
    private String descripcion;
    private int id_producto;
    private String url_imagen;
    private String url_video;



    public ModeloProducto(int id_producto,  String nombre, String descripcion, String url_imagen, String url_video) {
        this.id_producto = id_producto;
        this.url_imagen = url_imagen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.url_video = url_video;
    }

    public ModeloProducto(int id_producto, String nombre){
        this.nombre = nombre;
        this.id_producto = id_producto;

    }

    public int getId_producto(){return id_producto;}

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    @Override
    public String toString() {
        return getNombre();
    }

    public Drawable convertir_url_imagen(String url1)  {
        try {
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Drawable d = new BitmapDrawable(myBitmap);
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
