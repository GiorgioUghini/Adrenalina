package network;

import javafx.application.Platform;
import models.Lobby;
import models.Match;
import models.player.Player;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.updates.NewPlayerUpdate;
import network.updates.PlayerDisconnectUpdate;
import utils.TokenGenerator;

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
            List<Response> updates = new LinkedList<>(currentPlayer.getUpdates());
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
        Player player = lobby.getPlayerByUsername(username);
        if(player == null){
            token = lobby.registerPlayer(username, token);
            player = lobby.getWaitingPlayer(token);
            lobby.addPlayer(player);
        }
        else{
            if(token == null)
                token = TokenGenerator.nextToken();
            player.setToken(token);
            lobby.addPlayer(player);
            Match match = lobby.getMatch(player);
            match.addUpdate(new NewPlayerUpdate());
        }
        if(Server.getInstance().getConnection().isSocket(token)){
            UpdatePusher updatePusher = new UpdatePusher(player);
            SocketWrapper socketWrapper = Server.getInstance().getConnection().getSocketWrapper(token);
            socketWrapper.setUpdatePusher(updatePusher);
            (new Thread(updatePusher)).start();
        }
        lobby.addUpdateWaitingPlayer( new NewPlayerUpdate());
        return new RegisterPlayerResponse(token);
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