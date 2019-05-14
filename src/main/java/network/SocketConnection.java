package network;

import network.requests.RegisterPlayerRequest;
import utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketConnection implements Connection {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String token;

    public SocketConnection() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void init() {
        try {
            socket = new Socket(Constants.HOSTNAME, Constants.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            ServerListener serverListener = new ServerListener();
            (new Thread(serverListener)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendRequest(Request request) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            request.setToken(token);
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void registerPlayer(String username) {
        RegisterPlayerRequest request = new RegisterPlayerRequest(username);
        sendRequest(request);
    }

    @Override
    public void receiveResponse(Response response) {

    }
}