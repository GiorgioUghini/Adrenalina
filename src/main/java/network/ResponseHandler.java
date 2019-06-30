package network;

import controllers.ScreenController;
import errors.InvalidInputException;
import errors.NotEnoughAmmoException;
import errors.NothingToGrabException;
import errors.WeaponCardException;
import models.card.LegitEffects;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnType;
import network.responses.*;
import network.updates.*;
import views.GameView;
import views.MenuView;

import java.util.List;

public class ResponseHandler implements ResponseHandlerInterface {

    @Override
    public void handle(RegisterPlayerResponse response) {
        Client clientInstance = Client.getInstance();
        clientInstance.getCurrentView().showMessage("Registrato come nuovo utente");
        clientInstance.setPlayerUsername(response.getMe());
    }

    @Override
    public void handle(ReconnectPlayerResponse response) {
        Client.getInstance().setReconnecting(true);
        Client.getInstance().setPlayers(response.players);
        Client.getInstance().setMap(response.map);
        Client.getInstance().setPlayer(response.player);
        ScreenController.getInstance().activate("GameRoom.fxml");
    }

    @Override
    public void handle(ValidActionsResponse response) {
        if (response.newActions) {
            Client.getInstance().setCurrentActionType(null);
        }

        GameView gameView = (GameView) Client.getInstance().getCurrentView();
        gameView.updateActions(response.actions);

        Client.getInstance().setActions(response.actions);
        if (response.actions.keySet().size() == 1 && Client.getInstance().getCurrentActionType() == null) { // auto-action
            ActionType actionType = response.actions.keySet().stream().findFirst().orElse(null);
            Client.getInstance().setCurrentActionType(actionType);
            Client.getInstance().getConnection().action(actionType);
        }
    }


    @Override
    public void handle(ErrorResponse response) {
        Client.getInstance().getCurrentView().printError("Errore gestito dal server"); //DEBUG
        response.exception.printStackTrace();
        try {
            throw response.exception;
        } catch (InvalidInputException inputEx) {
            String error = inputEx.getMessage();
            Client.getInstance().getCurrentView().printError(error);
        } catch (WeaponCardException ex) {
            Client.getInstance().getConnection().finishCard();
        }catch(NotEnoughAmmoException e) {
            Client.getInstance().getCurrentView().showMessage("You need more ammos to do this");
            Client.getInstance().getConnection().validActions();
        } catch(NothingToGrabException e){
            Client.getInstance().getCurrentView().showMessage("Nothing to grab here. Maybe you do not have enough ammos?");
            Client.getInstance().getConnection().validActions();
        } catch (Exception ex) {
            String error = ex.getMessage();
            Client.getInstance().getCurrentView().printError(error);
            Client.getInstance().getConnection().validActions();
        }
    }

    @Override
    public void handle(StartGameUpdate response) {
        Client.getInstance().setPlayers(response.players);
    }

    @Override
    public void handle(PlayerDisconnectUpdate response) {
        Client.getInstance().getCurrentView().onPlayerDisconnected(response.getName());
        //IF WE ARE IN MENU VIEW:

    }

    @Override
    public void handle(WaitingPlayerResponse response) {
        ((MenuView) Client.getInstance().getCurrentView()).showWaitingPlayerList(response.getList());
    }

    @Override
    public void handle(CardEffectsResponse response) {
        LegitEffects legitEffects = response.legitEffects;
        ((GameView) Client.getInstance().getCurrentView()).effectChoosingDialog(legitEffects);
    }

    @Override
    public void handle(NewPlayerUpdate response) {
        //IF WE ARE IN MENU VIEW:
        ((MenuView) Client.getInstance().getCurrentView()).onNewPlayer(response.getName());
    }

    @Override
    public void handle(ChooseMapUpdate response) {
        ((MenuView) Client.getInstance().getCurrentView()).chooseMap(response.getUsername());
    }

    @Override
    public void handle(MapChosenUpdate response) {
        ((MenuView) Client.getInstance().getCurrentView()).mapChosen(response.mapIndex);
        Client.getInstance().setMap(response.map);
        Client.getInstance().setCurrentTurnType(TurnType.START_GAME);
    }

    @Override
    public void handle(NextTurnUpdate response) {
        Client client = Client.getInstance();
        client.setMyTurn(response.name.equals(client.getPlayerUsername()));
        try {
            ((GameView)client.getCurrentView()).startTurn(response.name);
        } catch (Exception e) {
            Client.getInstance();
        }
    }

    @Override
    public void handle(ChooseMapResponse response) {
        //should be empty
    }

    @Override
    public void handle(DrawPowerUpResponse response) {
        Client.getInstance().getCurrentView().showMessage("You drawn " + response.getCard().name + " and its color is " + response.getCard().color.name());
    }

    @Override
    public void handle(SpawnPlayerResponse response) {
        Client.getInstance().getCurrentView().showMessage("You've successfully spawned!");
        Client.getInstance().setCurrentActionType(null);
        ((GameView) Client.getInstance().getCurrentView()).getValidActions();
        Client.getInstance().setCurrentTurnType(TurnType.IN_GAME);
    }

    @Override
    public void handle(SelectResponse response) {
        ((GameView) Client.getInstance().getCurrentView()).selectTag(response.selectable);
        // response.selectable per ottenere type e oggetti da selezionare
    }

    @Override
    public void handle(PowerUpSelectResponse response) {

    }

    @Override
    public void handle(FinishCardResponse response) {
        Client client = Client.getInstance();
        client.setShowEvents(true);
        client.setCurrentActionType(null);
        client.getConnection().validActions();
    }

    @Override
    public void handle(FinishEffectResponse response) {
        ((GameView) Client.getInstance().getCurrentView()).continueWeapon();
    }

    @Override
    public void handle(FinishPowerUpResponse response) {

    }

    @Override
    public void handle(EndTurnResponse response) {

    }

    @Override
    public void handle(GrabResponse response) {
        Client.getInstance().setShowEvents(true);
        Client.getInstance().getConnection().validActions();
    }

    @Override
    public void handle(RunResponse response) {
        Client.getInstance().setShowEvents(true);
        Client.getInstance().getConnection().validActions();
    }

    @Override
    public void handle(ReloadResponse response) {

    }

    @Override
    public void handle(TurnActionResponse response) {
        Client.getInstance().setShowEvents(true);
        Client.getInstance().getConnection().validActions();
    }

    @Override
    public void handle(MapUpdate response) {
        ((GameView) Client.getInstance().getCurrentView()).updateMapView(response.map);
        Client.getInstance().setMap(response.map);
    }

    @Override
    public void handle(PlayerUpdate response) {
        Client client = Client.getInstance();
        Player me = client.getPlayer();

        if (me.getName().equals(response.player.getName())) {
            ((GameView) Client.getInstance().getCurrentView()).updatePlayerView(response.player);
            Client.getInstance().setPlayer(response.player);
        }
    }

    @Override
    public void handle(ReconnectResponse response) {

    }

    @Override
    public void handle(PlayersUpdate response) {
        Client.getInstance().setPlayers(response.players);
    }

    @Override
    public void handle(DamageUpdate response) {
        List<Player> players = Client.getInstance().getPlayers();
        if (Client.getInstance().getPlayer().getName().equals(response.player.getName())) {
            ((GameView) Client.getInstance().getCurrentView()).updatePlayerView(response.player);
            players.set(players.indexOf(response.player), response.player);
        }
        ((GameView) Client.getInstance().getCurrentView()).onDamage(response.player);
    }

    @Override
    public void handle(MarkUpdate response) {
        List<Player> players = Client.getInstance().getPlayers();
        if (Client.getInstance().getPlayer().getName().equals(response.player.getName())) {
            ((GameView) Client.getInstance().getCurrentView()).updatePlayerView(response.player);
            players.set(players.indexOf(response.player), response.player);
        }
        ((GameView) Client.getInstance().getCurrentView()).onMark(response.player);
    }

    @Override
    public void handle(PointsUpdate response) {
        ((GameView) Client.getInstance().getCurrentView()).updatePoints(response.points);
    }

    @Override
    public void handle(EndMatchUpdate response) {
        //TODO show response.points
    }
}
