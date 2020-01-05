package Operaciones;

import org.json.JSONObject;

import java.io.File;

public class CreateDir {

    public CreateDir() {
    }

    public JSONObject doCreateDir(JSONObject data){


        JSONObject respuesta = new JSONObject();

        try {

            String pwd = "/home/cverde/REDES/USERS/" + data.getString("pwd");

            File userDir = new File(pwd);

            if (userDir.mkdir()){
                respuesta.put("R", "0");
                respuesta.put("M", "Directorio creado exitosamente.");
                respuesta.put("pwd", pwd);
            }
            else{

                respuesta.put("R", "1");
                respuesta.put("M", "No se pudo crear el directorio.");
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
