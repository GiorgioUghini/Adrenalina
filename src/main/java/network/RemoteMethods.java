package network;

public class RemoteMethods implements RemoteMethodsInterface {
    @Override
    public Object longPoll() {
        return null;
    }

    @Override
    public String registerPlayer(String username) {
        // TODO add token login
        return "abc123";
    }
}