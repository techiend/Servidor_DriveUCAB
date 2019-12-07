package Operaciones;

import DBHelper.DBClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Login {

    public Login() {
    }

    public JSONObject doLogin(JSONObject data){

        JSONObject respuesta = new JSONObject();

        try (
                Connection conn = DBClass.getConn();
                PreparedStatement getUser = conn.prepareStatement("SELECT * FROM users WHERE user_email = ?")
        ){

            getUser.setString(1, data.getString("email"));
            ResultSet rs = getUser.executeQuery();

            if (rs.next()){

                if (rs.getString("user_pass").equals(data.getString("p"))){

                    long maxSpace = rs.getLong("user_maxspace");

                    respuesta.put("R", "0");
                    respuesta.put("M", "Login con exito.");
                    respuesta.put("u_id", rs.getInt("user_id"));
                    respuesta.put("u_name", rs.getString("user_name"));
                    respuesta.put("u_lname", rs.getString("user_lname"));
                    respuesta.put("u_max", manageSize(maxSpace));
                    respuesta.put("u_pwd", data.getString("email")+"/");

                    File userDir = new File("/home/techiend/REDES/USERS/"+data.getString("email")+"/");

                    File[] dirFiles = userDir.listFiles();

                    JSONArray userFiles = new JSONArray();

                    long totalFilesSize = 0;

                    for (File file : dirFiles) {
                        String fileName = file.getName();
                        totalFilesSize += file.length();

                        JSONObject userFile = new JSONObject();
                        userFile.put("f_name", fileName);
                        userFile.put("f_size", manageSize(file.length()));
                        userFile.put("f_dir", file.isDirectory());

                        DateFormat dateFormater = new SimpleDateFormat("dd-MM-yyy HH:mm:ss a");
                        userFile.put("f_lastMod", dateFormater.format(file.lastModified()));

                        userFiles.put(userFile);

                    }

                    System.out.println("total tamaño archivos: " + totalFilesSize);
                    System.out.println("total tamaño completo: " + maxSpace);
                    System.out.println("restante: "+ (maxSpace-totalFilesSize));

                    respuesta.put("u_used", manageSize(totalFilesSize));
                    respuesta.put("u_free", manageSize((maxSpace-totalFilesSize)));
                    respuesta.put("files", userFiles);

                }
                else{
                    respuesta.put("R", "-1");
                    respuesta.put("M", "Usuario/Clave incorrecto.");
                }

            }
            else {
                respuesta.put("R", "-1");
                respuesta.put("M", "Usuario no existe.");
            }

            return respuesta;

        }
        catch (PSQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
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
