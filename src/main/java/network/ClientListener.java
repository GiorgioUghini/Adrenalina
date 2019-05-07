package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener implements Runnable{

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RequestHandler requestHandler;

    public ClientListener(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.requestHandler = new ServerController();
    }

    @Override
    public void run() {
        try {
            Request request = (Request) in.readObject();
            Response response = request.handle(requestHandler);
            out.writeObject(response);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
