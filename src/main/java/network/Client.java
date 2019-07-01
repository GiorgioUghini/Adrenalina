package network;

import errors.InvalidViewTypeException;
import models.map.Coordinate;
import models.map.GameMap;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;
import models.turn.TurnType;
import views.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private static Client instance = null;

    private boolean debug;
    private String hostname;
    private int socketPort;
    private int registryPort;
    private Connection connection;
    private ViewType viewType;
    private MenuView menuView;
    private GameView gameView;
    private View currentView;
    private String me;
    private boolean reconnecting;
    private boolean isMyTurn;
    private Map<ActionType, List<TurnEvent>> actions;
    private TurnType currentTurnType;
    private ActionType currentActionType;
    private GameMap map;
    private List<Player> players;
    private boolean showEvents = false;

    private HashMap<Player, Coordinate> playerCoordinateMap = new HashMap<>();

    private int mapNum;

    public int getMapNum() {
        return mapNum;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public static Client getInstance()
    {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private Client() {

    }

    public Map<Player, Coordinate> getPlayerCoordinateMap() {
        return playerCoordinateMap;
    }

    public TurnType getCurrentTurnType(){
        return currentTurnType;
    }

    public String getPlayerUsername() {
        return me;
    }

    public Player getPlayer() {
        return players != null ? players.stream().filter(p->p.getName().equals(getPlayerUsername())).findFirst().orElse(null) : null;
    }

    public void setPlayer(Player me) {
        players.set(players.indexOf(me), me);
    }

    public void setPlayerUsername(String me) {
        this.me = me;
    }

    public void setActions(Map<ActionType, List<TurnEvent>> actions){
        this.actions = actions;
    }

    public Map<ActionType, List<TurnEvent>> getActions(){
        return actions;
    }

    public void setCurrentTurnType(TurnType type){
        this.currentTurnType = type;
    }

    public void setCurrentActionType(ActionType type){
        this.currentActionType = type;
    }

    public ActionType getCurrentActionType(){
            return currentActionType;
        }

    public void activateGameViewCLI() {
        gameView = new GameViewCLI();
        currentView = gameView;
    }

    public void start(ViewType viewType, String hostname, int socketPort, int registryPort) throws InterruptedException {
        this.viewType = viewType;
        this.hostname = hostname;
        this.socketPort = socketPort;
        this.registryPort = registryPort;
        if(viewType == ViewType.CLI){
            menuView = new MenuViewCLI();
        }
        else if(viewType == ViewType.GUI){
            menuView = new MenuViewGUI();
        }
        else{
            throw new InvalidViewTypeException();
        }
        currentView = menuView;
        menuView.startView();
        //Thread.currentThread().join();
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

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public boolean getShowEvents() {
        return showEvents;
    }

    public void setShowEvents(boolean showEvents) {
        this.showEvents = showEvents;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isReconnecting() {
        return reconnecting;
    }

    public void setReconnecting(boolean reconnecting) {
        this.reconnecting = reconnecting;
    }

    public String getHostname() {
        return hostname;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public int getRegistryPort() {
        return registryPort;
    }
}
