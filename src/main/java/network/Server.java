package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean stop;

    public Server(int port) throws IOException {
        stop = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        while (!stop) {
            Socket clientSocket = serverSocket.accept();
            pool.submit(new ClientListener(clientSocket));
        }
    }

    public void stop() throws IOException {
        stop = true;
        serverSocket.close();
        pool.shutdown();
    }
}
