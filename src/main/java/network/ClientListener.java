package network;

import utils.TokenGenerator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener implements Runnable{

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RequestHandlerInterface requestHandler;
    private String token;

    public ClientListener(Socket socket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(socket);
        this.token = TokenGenerator.nextToken();
        Server.getInstance().getConnection().addSocket(token, socketWrapper);
        this.out = socketWrapper.getOutputStream();
        this.in = socketWrapper.getInputStream();
        this.requestHandler = new RequestHandler();
    }

    @Override
    public void run() {
        try {
            Request request = (Request) in.readObject();
            request.setToken(token);
            Response response = request.handle(requestHandler);
            out.writeObject(response);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
