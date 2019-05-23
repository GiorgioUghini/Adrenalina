package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketWrapper {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private UpdatePusher updatePusher;

    public SocketWrapper(Socket socket){
        this.socket = socket;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
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

    public Socket getSocket(){
        return socket;
    }

    public synchronized void write(Object object) throws IOException {
        out.writeObject(object);
    }

    public void setUpdatePusher(UpdatePusher updatePusher){
        this.updatePusher = updatePusher;
    }

    public void stopUpdatePusher(){
        updatePusher.stop();
    }
}
