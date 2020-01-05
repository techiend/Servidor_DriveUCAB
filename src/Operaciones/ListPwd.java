package Operaciones;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ListPwd {

    public ListPwd() {
    }

    public JSONObject doList(JSONObject data){

        JSONObject respuesta = new JSONObject();

        try {

            File userDir = new File("/home/cverde/REDES/USERS/" + data.getString("pwd"));
            File[] dirFiles = userDir.listFiles();

            JSONArray userFiles = new JSONArray();

            JSONObject userFile = new JSONObject();

            if (!data.getString("pwd").equals(data.getString("email")+"/")) {
                userFile.put("f_name", "..");
                userFile.put("f_size", manageSize(0));
                userFile.put("f_dir", true);
                userFile.put("f_lastMod", "");

                userFiles.put(userFile);
            }

            for (File file : dirFiles) {
                String fileName = file.getName();

                userFile = new JSONObject();
                userFile.put("f_name", fileName);
                userFile.put("f_size", manageSize(file.length()));
                userFile.put("f_dir", file.isDirectory());

                DateFormat dateFormater = new SimpleDateFormat("dd-MM-yyy HH:mm:ss a");
                userFile.put("f_lastMod", dateFormater.format(file.lastModified()));

                userFiles.put(userFile);

            }

            respuesta.put("R", "0");
            respuesta.put("M", "Se han listado sus archivos correctamente.");
            respuesta.put("pwd", data.getString("pwd"));
            respuesta.put("files", userFiles);
            return respuesta;
        }
        catch (Exception e){
            e.printStackTrace();

            respuesta.put("R", "1");
            respuesta.put("M", "No se encuentra la ruta solicitada.");
        }

        return respuesta;
    }

    private String manageSize(long fileSize) {

        String size = "";

        long kb = 1024;
        long mb = kb*1024;
        long gb = mb*1024;

        if (fileSize < kb) { // Es Bytes
            size = Long.toString(fileSize) + " B";
        }
        if (fileSize >= kb && fileSize< mb){ // Es KiloBytes
            size = Double.toString(fileSize/kb) + " KB";
        }
        if (fileSize >= mb && fileSize < gb){ // Es MegaBytes
            size = Double.toString((double)(fileSize/kb)/kb) + " MB";
        }
        if (fileSize >= gb) { // Es GigaBytes
            size = Double.toString((double)((fileSize/kb)/kb)/kb) + " GB";
        }

        return size;
    }
}
