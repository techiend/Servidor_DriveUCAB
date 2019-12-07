package Operaciones;

import DBHelper.DBClass;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.sql.*;

public class Registro {

    public Registro() {
    }

    public JSONObject doRegistro(JSONObject data){

        JSONObject respuesta = new JSONObject();

        try (
                Connection conn = DBClass.getConn();
                PreparedStatement pst = conn.prepareStatement("INSERT INTO users (user_name, user_lname, user_email, user_pass) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement delUser = conn.prepareStatement("DELETE FROM users WHERE user_email = ?")
        ){

            pst.setString(1, data.getString("name"));
            pst.setString(2, data.getString("lname"));
            pst.setString(3, data.getString("email"));
            pst.setString(4, data.getString("p"));

            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();

            if (rs.next()){

                File file = new File("/home/techiend/REDES/USERS/"+data.getString("email"));

                if (file.mkdir()) {

                    respuesta.put("R", "0");
                    respuesta.put("M", "El usuario " + data.getString("email") + " fue registrado correctamente con el id(" + rs.getInt(1) + ").");

                }
                else {
                    System.out.println("No se pudo crear el directorio");
                    delUser.setString(1, data.getString("email"));
                    delUser.executeUpdate();

                    respuesta.put("R", "-1");
                    respuesta.put("M", "El usuario no pudo ser registrado.");
                }

            }else{

                respuesta.put("R", "-1");
                respuesta.put("M", "El usuario no pudo ser registrado.");

            }

            return respuesta;

        }
        catch (PSQLException e){
            if (e.getMessage().contains("duplicate key value")){
                respuesta.put("R", "-1");
                respuesta.put("M", "El usuario ya se encuentra registrado.");

                System.out.println(e.getMessage());

                return respuesta;
            }
            else {
                respuesta.put("R", "-1");
                respuesta.put("M", "Error en la base de datos.");

                e.printStackTrace();
                return respuesta;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        respuesta.put("R", "-1");
        respuesta.put("M", "Error, su solicitud no pudo ser procesada.");
        return respuesta;

    }
}
