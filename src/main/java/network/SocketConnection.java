package network;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.Square;
import models.player.Ammo;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.requests.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;


public class SocketConnection implements Connection {

    private ResponseHandler responseHandler;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String token;

    @Override
    public void registerPlayer(String username, String password) {
        try {
            RegisterPlayerRequest request = new RegisterPlayerRequest(username, password);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void getWaitingPlayer() {
        try {
            WaitingPlayerRequest request = new WaitingPlayerRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void validActions() {
        try {
            ValidActionsRequest request = new ValidActionsRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void chooseMap(int map) {
        try {
            ChooseMapRequest request = new ChooseMapRequest(map);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void cardEffects(String cardName) {
        try {
            CardEffectsRequest request = new CardEffectsRequest(cardName);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void drawPowerUpCard() {
        try {
            DrawPowerUpRequest request = new DrawPowerUpRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void spawnPlayer(PowerUpCard powerUpCard) {
        try {
            SpawnPlayerRequest request = new SpawnPlayerRequest(powerUpCard);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void tagElement(Taggable taggable) {
        try {
            TagElementRequest request = new TagElementRequest(taggable);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void powerUpTagElement(Taggable taggable) {
        try {
            PowerUpTagElementRequest request = new PowerUpTagElementRequest(taggable);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard) {
        try {
            PlayEffectRequest request = new PlayEffectRequest(effect, ammo, powerUpCard);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void finishCard() {
        try {
            FinishCardRequest request = new FinishCardRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void endTurn() {
        try {
            EndTurnRequest request = new EndTurnRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        try {
            GrabRequest request = new GrabRequest(drawn, toRelease, powerUpCard);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void run(TurnEvent turnEvent, Square square) {
        try {
            RunRequest request = new RunRequest(turnEvent, square);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void reload(Map<WeaponCard, PowerUpCard> weaponsMap) {
        try {
            ReloadRequest request = new ReloadRequest(weaponsMap);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void action(ActionType actionType) {
        try {
            TurnActionRequest request = new TurnActionRequest(actionType);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void playPowerUp(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) {
        try {
            PlayPowerUpRequest request = new PlayPowerUpRequest(powerUpName, ammo, powerUpAmmo);
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void reconnect() {
        try {
            ReconnectRequest request = new ReconnectRequest();
            write(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    @Override
    public void receiveResponse(Response response) {
        response.handle(responseHandler);
    }

    public void init() {
        try {
            socket = new Socket(Client.getInstance().getHostname(), Client.getInstance().getSocketPort());
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            responseHandler = new ResponseHandler();
            ServerListener serverListener = new ServerListener();
            (new Thread(serverListener)).start();
        } catch (IOException e) {
            Logger.getAnonymousLogger().info(e.toString());
        }
    }

    public synchronized void write(Object object) throws IOException {
        out.writeObject(object);
        out.flush();
        out.reset();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }
}