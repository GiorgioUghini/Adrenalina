package network;

import models.Lobby;
import models.Match;
import models.player.Player;
import network.responses.*;
import network.updates.MapChosenUpdate;
import network.updates.NewPlayerUpdate;
import utils.TokenGenerator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException { }

    @Override
    public List<Response> longPolling(String token)  {
        if(token != null){
            RMIWrapper rmiWrapper = Server.getInstance().getConnection().getRMIWrapper(token);
            rmiWrapper.ping();
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
    public Response registerPlayer(String username, String password, String token)  {
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
            match.addUpdate(new NewPlayerUpdate(player.getName()));
        }
        if(Server.getInstance().getConnection().isSocket(token)){
            //TODO: Put UpdatePusher initialization on socket connection, not player registration. Oor listview won't be refreshed
            UpdatePusher updatePusher = new UpdatePusher(player);
            SocketWrapper socketWrapper = Server.getInstance().getConnection().getSocketWrapper(token);
            socketWrapper.setUpdatePusher(updatePusher);
            (new Thread(updatePusher)).start();
        }
        else {
            RMIWrapper rmiWrapper = new RMIWrapper();
            Server.getInstance().getConnection().addRMI(token, rmiWrapper);
            RMIStatusTask rmiStatusTask = new RMIStatusTask(player);
            rmiWrapper.setRMIStatusTask(rmiStatusTask);
            Timer timer = new Timer();
            timer.schedule(rmiStatusTask, 0, 4000);
        }
        lobby.addUpdateWaitingPlayer( new NewPlayerUpdate(player.getName()));
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

    @Override
    public WaitingPlayerResponse waitingPlayer() throws RemoteException {
        List<String> waitingPlayers = Server.getInstance().getLobby().getWaitingPlayersUsername();
        return new WaitingPlayerResponse(waitingPlayers);
    }

    @Override
    public Response chooseMap(String token, int map) throws RemoteException {
        Server.getInstance().getLobby().getMatch(token).createMap(map);
        Server.getInstance().getLobby().getMatch(token).addUpdate(new MapChosenUpdate(map));
        return new ChooseMapResponse();
    }

    @Override
    public Response cardEffects(String token, String cardName) throws RemoteException {
        return null;
    }
}