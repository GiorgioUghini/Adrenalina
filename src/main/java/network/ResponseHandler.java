package network;

import errors.InvalidInputException;
import models.turn.ActionElement;
import network.responses.*;
import network.updates.*;
import views.MenuView;

import java.util.List;

public class ResponseHandler implements ResponseHandlerInterface {
    @Override
    public void handle(RegisterPlayerResponse response) {
        Client clientInstance = Client.getInstance();
        clientInstance.getConnection().setToken(response.token);
        clientInstance.getCurrentView().showMessage("Token: " + response.token);
    }

    @Override
    public void handle(ValidActionsResponse response) {
        if(!response.actions.isEmpty()){
            Client.getInstance().getCurrentView().showMessage("Your valid actions are:");
            for(List<ActionElement> list : response.actions){
                for(ActionElement a : list){
                    Client.getInstance().getCurrentView().showMessage(a.name());
                }
                Client.getInstance().getCurrentView().showMessage("\n");
            }
        }
        else{
            Client.getInstance().getCurrentView().showMessage("NO actions for you!");
        }
    }

    @Override
    public void handle(ErrorResponse response) {
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
        //NOTHING TO DO
    }


}
