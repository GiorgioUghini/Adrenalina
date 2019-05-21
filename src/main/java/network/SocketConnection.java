package network;

import network.requests.RegisterPlayerRequest;
import network.requests.ValidActionsRequest;
import utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketConnection implements Connection {

    private ResponseHandler responseHandler;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String token;

    @Override
    public void registerPlayer(String username) {
        try {
            RegisterPlayerRequest request = new RegisterPlayerRequest(username);
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validActions() {
        try {
            ValidActionsRequest request = new ValidActionsRequest();
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveResponse(Response response) {
        response.handle(responseHandler);
    }

    public void init() {
        try {
            socket = new Socket(Constants.HOSTNAME, Constants.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            responseHandler = new ResponseHandler();
            ServerListener serverListener = new ServerListener();
            (new Thread(serverListener)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
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
}