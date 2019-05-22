package network;

import errors.InvalidInputException;
import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;

import java.util.List;

public class ResponseHandler implements ResponseHandlerInterface {
    @Override
    public void handle(RegisterPlayerResponse response) {
        Client.getInstance().getConnection().setToken(response.token);
        Client.getInstance().getCurrentView().showMessage("Token: " + response.token);
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
}
