package network;

public class RegisterPlayerResponse implements Response {

    public String token;

    public RegisterPlayerResponse(String token){
        this.token = token;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}