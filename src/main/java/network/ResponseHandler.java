package network;

import errors.InvalidInputException;
import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.updates.NewPlayerUpdate;
import network.updates.PlayerDisconnectUpdate;
import network.updates.StartGameUpdate;
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
        Client.getInstance().getCurrentView().showMessage("Inizio gioco");
    }

    @Override
    public void handle(PlayerDisconnectUpdate response) {
        Client.getInstance().getCurrentView().showMessage("A player disconnected");
    }

    @Override
    public void handle(NewPlayerUpdate response) {
        Client.getInstance().getCurrentView().showMessage("Heyyyy");
    }
}
