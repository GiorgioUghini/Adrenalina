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
    private RequestHandlerInterface requestHandler;

    public ClientListener(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.requestHandler = new RequestHandler();
    }

    @Override
    public void run() {
        try {
            Request request = (Request) in.readObject();
            String token = Server.getInstance().getConnection().getToken(socket);
            request.setToken(token);
            Response response = request.handle(requestHandler);
            out.writeObject(response);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
