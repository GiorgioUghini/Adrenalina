package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            e.printStackTrace();
        }
    }
}
