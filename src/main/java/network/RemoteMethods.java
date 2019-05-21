package network;

import models.Lobby;
import models.Match;
import models.player.Player;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException { }

    @Override
    public List<Update> longPolling(String token)  {
        if(token != null){
            Server server = Server.getInstance();
            Match currentMatch = Lobby.getInstance().getMatchByToken(token);
            Player currentPlayer = currentMatch.getPlayerByToken(token);
            List<Update> updates = currentPlayer.getUpdates();
            currentPlayer.clearUpdates();
            return updates;
        }
        else{
            return new LinkedList<Update>();
        }
    }

    @Override
    public RegisterPlayerResponse registerPlayer(String username, String token)  {
        Lobby mainLobby = Lobby.getInstance();
        String returnToken = mainLobby.registerPlayer(username, token);
        return new RegisterPlayerResponse(returnToken);
    }

    @Override
    public ValidActionsResponse validActions(String token) throws RemoteException {
        Server server = Server.getInstance();
        Match currentMatch = Lobby.getInstance().getMatchByToken(token);
        Player currentPlayer = currentMatch.getPlayerByToken(token);
        Set actions = currentMatch.getPossibleAction(currentPlayer);
        return new ValidActionsResponse(actions);
    }

}