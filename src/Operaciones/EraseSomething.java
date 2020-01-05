package Operaciones;

import org.json.JSONObject;

import java.io.File;

public class EraseSomething {

    public EraseSomething() {
    }

    public JSONObject doErase(JSONObject data){


        JSONObject respuesta = new JSONObject();

        try {

            File somethingToErase = new File("/home/cverde/REDES/USERS/" + data.getString("pwd"));

            String[] pwd = data.getString("pwd").split("/");

            if (somethingToErase.delete()){
                respuesta.put("R", "0");
                respuesta.put("M", pwd[pwd.length-1]+" fue borrado exitosamente.");
            }
            else{

                respuesta.put("R", "1");
                respuesta.put("M", "No se pudo borrar "+ pwd[pwd.length-1]);
            }

            return respuesta;
        }
        catch (Exception e){
            e.printStackTrace();

            respuesta.put("R", "1");
            respuesta.put("M", "No se encuentra la ruta solicitada.");
        }

        return respuesta;

    }
}
