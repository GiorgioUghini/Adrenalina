package network;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.Square;
import models.player.Ammo;
import models.turn.ActionType;
import models.turn.TurnEvent;
import utils.Console;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class RMIConnection implements Connection {

    private RemoteMethodsInterface remoteMethods;
    private ResponseHandler responseHandler;
    private Registry registry;
    private BlockingQueue<Response> queue;
    private BlockingQueue<Thread> threadQueue;
    private String stringToken;

    @Override
    public void registerPlayer(String username, String password) {
        try {
            Response response = remoteMethods.registerPlayer(username, password, stringToken);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void getWaitingPlayer() {
        try {
            Response response = remoteMethods.waitingPlayer();
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void validActions() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.validActions(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void chooseMap(int map) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.chooseMap(token, map);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void cardEffects(String cardName) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.cardEffects(token, cardName);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void drawPowerUpCard() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.drawPowerUp(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void spawnPlayer(PowerUpCard powerUpCard) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.spawnPlayer(token, powerUpCard);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void tagElement(Taggable taggable) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.tagElement(token, taggable);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void powerUpTagElement(Taggable taggable) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.powerUpTagElement(token, taggable);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.playEffect(token, effect, ammo, powerUpCard);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void finishCard() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.finishCard(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void endTurn() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.endTurn(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.grab(token, drawn, toRelease, powerUpCard);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void run(TurnEvent turnEvent, Square square) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.run(token, turnEvent, square);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void reload(Map<WeaponCard, PowerUpCard> weaponsMap) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.reload(token, weaponsMap);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void action(ActionType actionType) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.action(token, actionType);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void playPowerUp(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.playPowerUp(token, powerUpName, ammo, powerUpAmmo);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void reconnect() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.reconnect(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void receiveResponse(Response response) {
        threadQueue.add(new Thread(() -> response.handle(responseHandler)));
    }

    @Override
    public void init() {
        try {
            System.setProperty("java.rmi.server.hostname", Client.getInstance().getHostname());
            registry = LocateRegistry.getRegistry(Client.getInstance().getHostname(), Client.getInstance().getRegistryPort());
            remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
            queue = new LinkedBlockingQueue<>(100);
            threadQueue = new LinkedBlockingQueue<>(100);
            responseHandler = new ResponseHandler();
            stringToken = remoteMethods.handshake();
            LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
            Timer timer = new Timer();
            timer.schedule(longPollingTask, 0, 300);
            PollingQueueListener pollingQueueListener = new PollingQueueListener(queue);
            (new Thread(pollingQueueListener)).start();
            ThreadQueueConsumer threadQueueConsumer = new ThreadQueueConsumer(threadQueue);
            (new Thread(threadQueueConsumer)).start();
        } catch (RemoteException | NotBoundException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public boolean isRMI() {
        return true;
    }

    @Override
    public void setToken(String token) {
        this.stringToken = token;
    }

    @Override
    public String getToken() {
        return stringToken;
    }

    public RemoteMethodsInterface getRemoteMethods() {
        return remoteMethods;
    }
}
