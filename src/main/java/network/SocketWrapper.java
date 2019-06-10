package network;

import utils.TokenGenerator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketWrapper implements ConnectionWrapper {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private UpdatePusher updatePusher;
    private String token;
    private BlockingQueue<Response> updates;

    public SocketWrapper(Socket socket){
        this.socket = socket;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.token = TokenGenerator.nextToken();
            updates = new LinkedBlockingQueue<>();
            Server.getInstance().getConnection().addConnectionWrapper(token, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    public Socket getSocket(){
        return socket;
    }

    public synchronized void write(Object object) throws IOException {
        out.writeObject(object);
        out.flush();
        out.reset();
    }

    public void setUpdatePusher(UpdatePusher updatePusher){
        this.updatePusher = updatePusher;
    }

    public void stop() {
        if(updatePusher != null)
            updatePusher.stop();
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUpdate(Response update) {
        updates.add(update);
    }

    @Override
    public BlockingQueue<Response> getUpdates() {
        return updates;
    }

    @Override
    public void clearUpdates() {
        updates.clear();
    }
}
