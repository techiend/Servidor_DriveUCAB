import DBHelper.DBClass;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {


    private static void signalHandlerkill()
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Murio #1");
            }
        });
    }

    private static void signalHandler()
    {
        SignalHandler signalHandler = new SignalHandler() {
            @Override
            public void handle(Signal signal) {

                if(signal.getNumber() == 2 || signal.getNumber() == 9) {

                    System.out.println("Murio #2");

                    System.exit(0);
                }

            }

        };
        Signal.handle(new Signal("INT"), signalHandler);
    }

    public static void initServer() {

        Runnable runnableS = () ->{
            try{
                new Start("24.63.57.111", 23315).startServer();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            catch (ParseException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        };

        new Thread(runnableS).start();

    }

    public static void main(String[] args) {

        signalHandler();
        signalHandlerkill();
        initServer();


    }

    public static void queryTest(){


        try (
                Connection conn = DBClass.getConn();
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM test")
        ){

            ResultSet rs = pst.executeQuery();

            while (rs.next()){

                System.out.println("nombre: " + rs.getString(2));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
