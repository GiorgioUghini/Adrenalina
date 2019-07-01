package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;

public class WaitingPlayerResponse implements Response {

    private List<String> waitingPlayerUsernames;

    public WaitingPlayerResponse(List<String> waitingPlayers){
        this.waitingPlayerUsernames = waitingPlayers;
    }

    public List<String> getList() { return waitingPlayerUsernames; }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}