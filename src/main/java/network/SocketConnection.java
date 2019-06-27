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
import network.requests.*;
import network.responses.CardEffectsResponse;
import network.responses.EndTurnResponse;
import network.responses.FinishCardResponse;
import network.responses.ReloadResponse;
import utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;


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
            e.printStackTrace();
        }
    }

    @Override
    public void getWaitingPlayer() {
        try {
            WaitingPlayerRequest request = new WaitingPlayerRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validActions() {
        try {
            ValidActionsRequest request = new ValidActionsRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseMap(int map) {
        try {
            ChooseMapRequest request = new ChooseMapRequest(map);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cardEffects(String cardName) {
        try {
            CardEffectsRequest request = new CardEffectsRequest(cardName);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawPowerUpCard() {
        try {
            DrawPowerUpRequest request = new DrawPowerUpRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawnPlayer(PowerUpCard powerUpCard) {
        try {
            SpawnPlayerRequest request = new SpawnPlayerRequest(powerUpCard);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tagElement(Taggable taggable) {
        try {
            TagElementRequest request = new TagElementRequest(taggable);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void powerUpTagElement(Taggable taggable) {
        try {
            PowerUpTagElementRequest request = new PowerUpTagElementRequest(taggable);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard) {
        try {
            PlayEffectRequest request = new PlayEffectRequest(effect, ammo, powerUpCard);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishCard() {
        try {
            FinishCardRequest request = new FinishCardRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endTurn() {
        try {
            EndTurnRequest request = new EndTurnRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        try {
            GrabRequest request = new GrabRequest(drawn,toRelease,powerUpCard);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(TurnEvent turnEvent, Square square) {
        try {
            RunRequest request = new RunRequest(turnEvent, square);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload(Map<WeaponCard, PowerUpCard> weaponsMap) {
        try {
            ReloadRequest request = new ReloadRequest(weaponsMap);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void action(ActionType actionType) {
        try {
            TurnActionRequest request = new TurnActionRequest(actionType);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playPowerUp(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) {
        try {
            PlayPowerUpRequest request = new PlayPowerUpRequest(powerUpName, ammo, powerUpAmmo);
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reconnect() {
        try {
            ReconnectRequest request = new ReconnectRequest();
            write(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveResponse(Response response) {
        response.handle(responseHandler);
    }

    public void init() {
        try {
            socket = new Socket(Constants.HOSTNAME, Constants.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            responseHandler = new ResponseHandler();
            ServerListener serverListener = new ServerListener();
            (new Thread(serverListener)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(Object object) throws IOException {
        out.writeObject(object);
        out.flush();
        out.reset();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
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