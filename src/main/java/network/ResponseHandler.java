package network;

import errors.InvalidInputException;
import models.map.RoomColor;
import models.turn.ActionElement;
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
    }

    @Override
    public void handle(ReconnectPlayerResponse response) {
        Client clientInstance = Client.getInstance();
        clientInstance.getCurrentView().showMessage("Ricollegato!");
        //TODO Set game state!!!
    }

    @Override
    public void handle(ValidActionsResponse response) {
        if(!response.actions.isEmpty()){
            Client.getInstance().getCurrentView().showMessage("You could do some actions. Chooose one from:");
            for(List<ActionElement> list : response.actions){

                switch (list.get(0)) {
                    case DRAW:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnDrawPowerUpVisibility(true);
                        break;
                    case RUN:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnRun(true);
                        break;
                    case SHOOT:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnShoot(true);
                        break;
                    case RELOAD:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnReload(true);
                        break;
                    case SPAWN:
                        ((GameView) Client.getInstance().getCurrentView()).setBtnSpawn(true);
                        break;
                    //NE MANCANO ALCUNI! TIPO I VARI USEPOWERUP
                }

                for(ActionElement a : list){
                    Client.getInstance().getCurrentView().showMessage(a.name());
                }
                Client.getInstance().getCurrentView().showMessage("\n");
            }
        }
        else{
            Client.getInstance().getCurrentView().showMessage("You can't really do any actions. Wait for your turn.");
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
        //TODO arrivata la risposta su client... vai avanti a giocare la carta
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
    }

    @Override
    public void handle(ChooseMapResponse response) {
        //should be empty
    }

    @Override
    public void handle(DrawPowerUpResponse response) {
        ((GameView) Client.getInstance().getCurrentView()).showMessage("You drawn " + response.getCard().name + " and its color is " + response.getCard().color.name());
    }

    @Override
    public void handle(SpawnPlayerResponse response) {
        //TODO bho non lo so sei spawnato (Vai Giorgio)
    }


}
