package network;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.RoomColor;
import models.map.Square;
import models.player.Ammo;
import models.turn.ActionType;
import models.turn.TurnEvent;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIConnection implements Connection {

    private RemoteMethodsInterface remoteMethods;
    private ResponseHandler responseHandler;
    private Registry registry;
    private BlockingQueue<Response> queue;
    private String token;

    @Override
    public void registerPlayer(String username, String password) {
        try {
            Response response = remoteMethods.registerPlayer(username, password, token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getWaitingPlayer() {
        try {
            Response response = remoteMethods.waitingPlayer();
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validActions() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.validActions(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseMap(int map) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.chooseMap(token, map);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cardEffects(String cardName) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.cardEffects(token, cardName);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawPowerUpCard() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.drawPowerUp(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawnPlayer(RoomColor color) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.spawnPlayer(token, color);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tagElement(Taggable taggable) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.tagElement(token, taggable);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void powerUpTagElement(Taggable taggable) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.powerUpTagElement(token, taggable);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.playEffect(token, effect, ammo, powerUpCard);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishCard() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.finishCard(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endTurn() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.endTurn(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void grab(WeaponCard drawn, WeaponCard toRelease) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.grab(token, drawn, toRelease);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(TurnEvent turnEvent, Square square) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.run(token, turnEvent, square);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload(List<WeaponCard> weapons) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.reload(token, weapons);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void action(ActionType actionType) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.action(token, actionType);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playPowerUp(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.playPowerUp(token, powerUpName, ammo, powerUpAmmo);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveResponse(Response response) {
        response.handle(responseHandler);
    }

    @Override
    public void init() {
        try {
            registry = LocateRegistry.getRegistry();
            remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
            queue = new LinkedBlockingQueue<>(100);
            responseHandler = new ResponseHandler();
            token = remoteMethods.handshake();
            LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
            Timer timer = new Timer();
            timer.schedule(longPollingTask, 0, 50);
            PollingQueueListener pollingQueueListener = new PollingQueueListener(queue);
            (new Thread(pollingQueueListener)).start();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    public RemoteMethodsInterface getRemoteMethods() {
        return remoteMethods;
    }
}
