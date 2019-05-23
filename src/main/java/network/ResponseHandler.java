package network;

import errors.InvalidInputException;
import models.turn.ActionElement;
import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.updates.NewPlayerUpdate;
import network.updates.PlayerDisconnectUpdate;
import network.updates.StartGameUpdate;
import views.MenuView;

import java.util.HashSet;
import java.util.LinkedList;
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
        Client.getInstance().getCurrentView().showMessage("Start game");
        Client.getInstance().getConnection().validActions();
    }

    @Override
    public void handle(PlayerDisconnectUpdate response) {
        Client.getInstance().getCurrentView().showMessage("A player disconnected");
    }

    @Override
    public void handle(NewPlayerUpdate response) {
        Client.getInstance().getCurrentView().showMessage("New player connected");
    }
}
