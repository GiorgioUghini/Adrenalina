package network;

import errors.InvalidInputException;
import javafx.application.Platform;
import models.card.Effect;
import models.card.LegitEffects;
import models.turn.ActionElement;
import models.turn.ActionType;
import models.turn.TurnEvent;
import models.turn.TurnType;
import network.responses.*;
import network.updates.*;
import views.GameView;
import views.MenuView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseHandler implements ResponseHandlerInterface {

    private boolean debug = true;
    private boolean debug2 = true;

    @Override
    public void handle(RegisterPlayerResponse response) {
        Client clientInstance = Client.getInstance();
        clientInstance.getCurrentView().showMessage("Registrato come nuovo utente");
        clientInstance.setPlayer(response.getMe());
    }

    @Override
    public void handle(ReconnectPlayerResponse response) {
        Client clientInstance = Client.getInstance();
        clientInstance.getCurrentView().showMessage("Ricollegato!");
        clientInstance.setPlayer(response.getMe());
        //TODO Set game state!!!
    }

    @Override
    public void handle(ValidActionsResponse response) {
        if(response.newActions){
            Client.getInstance().setCurrentActionType(null);
        }
        Client.getInstance().setActions(response.actions);
        if(response.actions.values().stream().filter(l -> !l.isEmpty()).count() > 0){
            Client.getInstance().getCurrentView().showMessage("You could do some actions. Chooose one from:");
            for(ActionType action : response.actions.keySet()){
                Client.getInstance().getCurrentView().showMessage("Action group " + action.name());
                Client.getInstance().getCurrentView().showMessage("\n");
            }
        }
        else{
            Client.getInstance().getCurrentView().showMessage("You can't really do any actions. Wait for your turn.");
        }
        if(response.actions.keySet().size() == 1 && Client.getInstance().getCurrentActionType() == null){
            ActionType actionType = response.actions.keySet().stream().findFirst().orElse(null);
            Client.getInstance().setCurrentActionType(actionType);
            Client.getInstance().getConnection().action(actionType);
        }
        if(Client.getInstance().getCurrentActionType() != null)
            for(TurnEvent turnEvent :  Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType())){
                switch (turnEvent) {
                    case DRAW:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnDrawPowerUpVisibility(true);
                        break;
                    case RUN_1:
                    case RUN_2:
                    case RUN_3:
                    case RUN_4:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnRunVisibility(true);
                        break;
                    case SHOOT:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnShootVisibility(true);
                        break;
                    case RELOAD:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnReloadVisibility(true);
                        break;
                    case SPAWN:
                        if( Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).size() == 1 &&  Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0) == TurnEvent.SPAWN)
                            ((GameView) Client.getInstance().getCurrentView()).setBtnSpawnVisibility(true);
                        break;
                    case GRAB:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnGrabAmmoVisibility(true);
                        break;
                    //NE MANCANO ALCUNI! TIPO I VARI USEPOWERUP
                }
                Client.getInstance().getCurrentView().showMessage(turnEvent.name());
                Client.getInstance().getCurrentView().showMessage("\n");
            }
        if(debug && Client.getInstance().getActions().keySet().contains(ActionType.RUN_NORMAL)){
            //TODO da spostare dopo
            Client.getInstance().setCurrentActionType(ActionType.RUN_NORMAL);
            Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
            debug = false;
            //TODO da spostare dopo
        }
    }

    @Override
    public void handle(ErrorResponse response) {
        Client.getInstance().getCurrentView().printError("Errore gestito dal server"); //DEBUG
        try {
            throw response.exception;
        }
        catch (InvalidInputException inputEx){
            String error = inputEx.getMessage();
            Client.getInstance().getCurrentView().printError(error);
        }
        catch (Exception ex){
            String error = ex.getMessage();
            Client.getInstance().getCurrentView().printError(error);
        }
    }

    @Override
    public void handle(StartGameUpdate response) {
        ((MenuView) Client.getInstance().getCurrentView()).startGame();
        //Client.getInstance().getConnection().validActions();
    }

    @Override
    public void handle(PlayerDisconnectUpdate response) {
        //IF WE ARE IN MENU VIEW:
        ((MenuView) Client.getInstance().getCurrentView()).onPlayerDisconnected(response.getName());
    }

    @Override
    public void handle(WaitingPlayerResponse response) {
        ((MenuView) Client.getInstance().getCurrentView()).showWaitingPlayerList(response.getList());
    }

    @Override
    public void handle(CardEffectsResponse response) {
        // TODO: Questo è solo un placeholder!!!! Bisogna far scegliere all'utente tra gli effect.name
        LegitEffects legitEffects = response.legitEffects;
        for(Effect effect : legitEffects.getLegitEffects()){
            Client.getInstance().getCurrentView().showMessage(effect.name);
        }
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
        ((MenuView) Client.getInstance().getCurrentView()).mapChosen(response.getMap());
        Client.getInstance().setCurrentTurnType(TurnType.START_GAME);
    }

    @Override
    public void handle(NextTurnUpdate response) {
        try {
            ((GameView) Client.getInstance().getCurrentView()).startTurn(response.name);
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
        ((GameView) Client.getInstance().getCurrentView()).addPowerUpToHand(response.getCard());
        Platform.runLater( () -> {
            Client.getInstance().getCurrentView().showMessage("You drawn " + response.getCard().name + " and its color is " + response.getCard().color.name());
        });
    }

    @Override
    public void handle(SpawnPlayerResponse response) {
        Client.getInstance().getCurrentView().showMessage("You've successfully spawned!");
        Client.getInstance().setCurrentActionType(null);
        ((GameView) Client.getInstance().getCurrentView()).getValidActions();
        Client.getInstance().setCurrentTurnType(TurnType.IN_GAME);
        //TODO bho non lo so sei spawnato (Vai Giorgio)
    }

    @Override
    public void handle(SelectResponse response) {
        // response.selectable per ottenere type e oggetti da selezionare
    }

    @Override
    public void handle(FinishCardResponse response) {
        // TODO andare avanti col turno sul client, è finito uno SHOOT
    }

    @Override
    public void handle(FinishEffectResponse response) {
        // TODO è finito un effetto, chiedere all'utente se vuole attivarne un altro o terminare lo SHOOT. Per terminare deve inviare una FinishCardRequest
    }

    @Override
    public void handle(EndTurnResponse response) {

    }

    @Override
    public void handle(GrabResponse response) {

    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    @Override
    public void handle(RunResponse response) {
        if(debug2){
            setTimeout(() -> {
                Client.getInstance().setCurrentActionType(ActionType.RUN_NORMAL);
                Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
            }, 3000);
            debug2 = false;
        }
    }

    @Override
    public void handle(ReloadResponse response) {

    }

    @Override
    public void handle(TurnActionResponse response) {
        Client.getInstance().getConnection().validActions();
    }


}
