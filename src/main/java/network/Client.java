package network;

import errors.InvalidViewTypeException;
import errors.NotImplementedException;
import errors.SingletionViolationException;
import views.*;

public class Client {

    private static Client instance = null;

    private Connection connection;
    private ViewType viewType;
    private MenuView menuView;
    private GameView gameView;
    private View currentView;

    public static Client getInstance()
    {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private Client() {

    }

    public void start(ViewType viewType) throws InterruptedException {
        this.viewType = viewType;
        if(viewType == ViewType.CLI){
            menuView = new MenuViewCLI();
            //gameView = new GameViewCLI(); Later
        }
        else if(viewType == ViewType.GUI){
            //TODO implementation GUI
            throw new NotImplementedException();
        }
        else{
            throw new InvalidViewTypeException();
        }
        currentView = menuView;
        menuView.createConnection();
        Thread.currentThread().join();
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection(){
        return connection;
    }

    public View getCurrentView(){
        return currentView;
    }
}
