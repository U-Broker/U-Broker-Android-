package utileria;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;

/**
 * Created by andresrodriguez on 30/05/17.
 */

public class Validaciones {


    //Valida si el valor contiene espacios u omite la arroba, de ser así devuelve false, de lo contrario, verdadero.
    public  boolean validarCorreo(String correo) {

        return !correo.contains(" ") && correo.contains("@");

    }

    public  boolean validarTamañoContraseña(String contraseña){
        return (contraseña.length()>5);
    }


    //Devuleve verdadero si la cadena esta vacia, de lo contrario, falso.
    public  boolean validarVacio(String cadena){
         return cadena.isEmpty();
    }



    public void validacionesFirebase(Context contexto, FirebaseAuthException e){
        switch (e.getErrorCode()){
            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(contexto, "El password no es correcto.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(contexto, "El formato del token es incorrecto, comuniquese con el administrador.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(contexto, "El formato del token corresponde a otra persona, comuniquese con el administrador.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(contexto, "La sesión contiene una token caducado o erroneo.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(contexto, "El dirección de correo electrónico es invalida o no es correcta.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(contexto, "La contraseña no es valida o el usuario no tiene una contraseña.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_MISMATCH":
                Toast.makeText(contexto, "Los datos de inicio de sesión no corresponden con la sesión que se tenía activa.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(contexto, "Esta operación requiere una auteticación reciente, inicia sesión de nuevo antes de reintentar esta operación.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(contexto, "Ya existe una cuenta con la misma dirección de correo eletrónico pero con diferentes credenciales de sesión.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(contexto, "Ya existe una cuenta con la misma dirección de correo eletrónico.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(contexto, "Esta credencial ya se encuenta en uso asociada con otra cuenta de usuario.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_DISABLED":
                Toast.makeText(contexto, "La cuenta se encuentra inactiva por el administrador.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(contexto, "La sesión ha expirado o no se encuentra valida. Inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(contexto, "No existe una cuenta registrada con estos datos.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(contexto, "Sesión no valida o expirada, por favor inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(contexto, "Está operación no está permitida.", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(contexto, "Ha ocurrido un error inesperado :(.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public boolean verificarRespuesta(String status) {
        boolean respuesta = true;
        if (status.toLowerCase().equals("error")) {
            respuesta = false;
        }

        return respuesta;
    }

    public String CapitalizarCadena(String sentence) {
        String words[] = sentence.replaceAll("\\s+", " ").trim().split(" ");
        String newSentence = "";
        for (String word : words) {
            for (int i = 0; i < word.length(); i++)
                newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase():
                        (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
        }

        return newSentence.trim();
    }

}
