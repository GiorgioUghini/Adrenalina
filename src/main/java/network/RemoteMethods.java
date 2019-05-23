package network;

import models.Lobby;
import models.Match;
import models.player.Player;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.updates.NewPlayerUpdate;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException { }

    @Override
    public List<Response> longPolling(String token)  {
        if(token != null){
            Player currentPlayer = Server.getInstance().getLobby().getPlayer(token);
            List<Response> updates = currentPlayer.getUpdates().stream().collect(Collectors.toList());
            currentPlayer.clearUpdates();
            return updates;
        }
        else{
            return new LinkedList<>();
        }
    }

    @Override
    public RegisterPlayerResponse registerPlayer(String username, String token)  {
        Lobby lobby = Server.getInstance().getLobby();
        String returnToken = lobby.registerPlayer(username, token);
        Player player = lobby.getWaitingPlayer(token);
        if(Server.getInstance().getConnection().isSocket(returnToken)){
            UpdatePusher updatePusher = new UpdatePusher(player);
            (new Thread(updatePusher)).start();
        }
        Response update = new NewPlayerUpdate();
        lobby.addUpdateWaitingPlayer(update);
        return new RegisterPlayerResponse(returnToken);
    }

    @Override
    public ValidActionsResponse validActions(String token) {
        Lobby lobby = Server.getInstance().getLobby();
        Player currentPlayer = lobby.getPlayer(token);
        Match currentMatch = lobby.getMatch(currentPlayer);
        Set actions = currentMatch.getPossibleAction(currentPlayer);
        return new ValidActionsResponse(actions);
    }
}