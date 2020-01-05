import Operaciones.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.util.*;


public class Start {

    private SocketChannel socketws;

    private Selector selector;
    private Selector selector_client;
    private InetSocketAddress listenAddress;
    private Thread client_thread;
    private int i =0;


    public Start(String ip , int port) throws IOException
    {
        listenAddress = new InetSocketAddress(ip, port);
    }

    public void startServer() throws IOException, InterruptedException, ParseException
    {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("[SERVIDOR] Servidor Activo...");


        while (true) {
            this.selector.select();
            Iterator keys = this.selector.selectedKeys().iterator();

            while (keys.hasNext() ) {
                SelectionKey key = (SelectionKey) keys.next();

                keys.remove();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                }
                else if (key.isReadable()) {
                    this.read(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException
    {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException, InterruptedException, ParseException
    {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(12280);
        int numRead = -1;
        try {
            numRead = channel.read(buffer);

            if (numRead == -1) {
//                System.out.println("numRead -1");
                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("Se ha desconectado "+remoteAddr.toString());
                channel.close();
                key.cancel();
                return;
            }


            byte[] data = new byte[numRead];

            System.arraycopy(buffer.array(), 0, data, 0, numRead);

            try {
                String command = new String(data);

                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("[SERVIDOR] Recibiendo de "+ remoteAddr.toString()+": '"+command.trim()+"'");

                JSONObject recibe = new JSONObject(command);

                switch (recibe.getString("acc")){

                    case "register":

                        doRegistro(recibe, channel, key);

                        break;
                    case "login":

                        doLogin(recibe, channel, key);


                        break;
                    case "create":

                        doCreate(recibe, channel, key);

                        break;
                    case "list":

                        doList(recibe, channel, key);

                        break;
                    case "erase":

                        doErase(recibe, channel, key);

                        break;

                }
            }
            catch (JSONException e){
                System.out.println("Formato incorrecto.");

                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                channel.close();
                key.cancel();
            }

        }catch (IOException e){
            System.out.println("Ay, se mato el parlante.");

            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            channel.close();
            key.cancel();
        }
    }



    // METODOS DE MANEJO DE PETICIONES

    private void doLogin(JSONObject recibe, SocketChannel channel, SelectionKey key) throws IOException {
        System.out.println("Login de usuario.\n");

//        PARA REALIZAR PRUEBAS CON TELNET DESCOMENTAR ESTO
//
//                        JSONObject dataLogin = new JSONObject();
//
//                        dataLogin.put("p","123456");
//                        dataLogin.put("email","cverde22@gmail.com");

        Login login= new Login();
        JSONObject respuestalogin= login.doLogin(recibe);

        System.out.println("Login responde: "+respuestalogin.toString(1));

        channel.write(ByteBuffer.wrap(respuestalogin.toString().getBytes("UTF-8")));

//        channel.shutdownOutput();
//        key.cancel();
    }

    public void doRegistro(JSONObject recibe, SocketChannel channel, SelectionKey key) throws IOException {


        System.out.println("Registrando usuario.\n");

//                        JSONObject dataRegistro = new JSONObject();
//
//                        dataRegistro.put("name","Alessandra");
//                        dataRegistro.put("lname","Varisco");
//                        dataRegistro.put("p","123456");
//                        dataRegistro.put("email","avarisco@gmail.com");

        Registro registro = new Registro();
        JSONObject respuestaregistro = registro.doRegistro(recibe);

        System.out.println("Registro responde: "+respuestaregistro.toString(1));

        channel.write(ByteBuffer.wrap(respuestaregistro.toString().getBytes("UTF-8")));

//        channel.shutdownOutput();
//        key.cancel();

    }

    public void doList(JSONObject recibe, SocketChannel channel, SelectionKey key) throws IOException {

        System.out.println("ListPwd de ruta.\n");

        System.out.println(recibe.toString(1));

//                        JSONObject dataLogin = new JSONObject();
//
//                        dataLogin.put("pwd","cverde22@gmail.com/testeo/");
//                        dataLogin.put("email","cverde22@gmail.com");
//
                        ListPwd listPwd= new ListPwd();
                        JSONObject respuestalist= listPwd.doList(recibe);

                        System.out.println("ListPwd responde: "+respuestalist.toString(1));

                        channel.write(ByteBuffer.wrap(respuestalist.toString().getBytes("UTF-8")));

//        channel.shutdownOutput();
//        key.cancel();
    }

    public void doCreate(JSONObject recibe, SocketChannel channel, SelectionKey key) throws IOException {

        System.out.println("Create directorio.\n");

        System.out.println(recibe.toString(1));

//                        JSONObject dataLogin = new JSONObject();
//
//                        dataLogin.put("pwd","cverde22@gmail.com/testeo/");
//                        dataLogin.put("email","cverde22@gmail.com");
//
        CreateDir createDir = new CreateDir();
        JSONObject respuestalist= createDir.doCreateDir(recibe);

        System.out.println("CreateDir responde: "+respuestalist.toString(1));

        channel.write(ByteBuffer.wrap(respuestalist.toString().getBytes("UTF-8")));

//        channel.shutdownOutput();
//        key.cancel();
    }

    public void doErase(JSONObject recibe, SocketChannel channel, SelectionKey key) throws IOException {

        System.out.println("Erase something.\n");

        System.out.println(recibe.toString(1));

//                        JSONObject dataLogin = new JSONObject();
//
//                        dataLogin.put("pwd","cverde22@gmail.com/testeo/");
//                        dataLogin.put("email","cverde22@gmail.com");
//
        EraseSomething eraseSomething = new EraseSomething();
        JSONObject respuestalist= eraseSomething.doErase(recibe);

        System.out.println("EraseSomething responde: "+respuestalist.toString(1));

        channel.write(ByteBuffer.wrap(respuestalist.toString().getBytes("UTF-8")));

//        channel.shutdownOutput();
//        key.cancel();
    }





}
