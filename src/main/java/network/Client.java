package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void start() throws IOException {
        ServerListener serverListener = new ServerListener(in);
        (new Thread(serverListener)).start();
    }

    public ObjectOutputStream getOutputStream(){
        return out;
    }

    public ObjectInputStream getInputStream(){
        return in;
    }

    public Socket getSocket(){
        return socket;
    }
}
