package network;

import errors.NotImplementedException;

public class ConnectionFactory {
    public Connection getConnection(ConnectionType type) {
        Connection connection = null;
        switch (type) {
            case Socket:
                connection = new SocketConnection();
                break;
            case RMI:
                connection = new RMIConnection();
                break;
            default:
                throw new NotImplementedException();
        }
        return connection;
    }
}
