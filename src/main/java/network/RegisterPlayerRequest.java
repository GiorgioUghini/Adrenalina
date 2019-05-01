package network;

public class RegisterPlayerRequest implements Request {

    public String username;

    public RegisterPlayerRequest(String username){
        this.username = username;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
