package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener implements Runnable{

    private ObjectInputStream in;
    private ResponseHandler responseHandler;

    public ServerListener(ObjectInputStream in) {
        this.in = in;
        this.responseHandler = new ClientController();
    }

    @Override
    public void run() {
        try {
            Response response = (Response) in.readObject();
            response.handle(responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
