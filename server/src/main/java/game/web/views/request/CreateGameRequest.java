package game.web.views.request;

import game.logic.GameMode;
import game.logic.util.SafeString;
import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CreateGameRequest extends ContextBasedRequestView {
    private final String prefix;
    private final SafeString gameName;
    private final SafeString playerName;
    private final GameMode gameMode;
    private final int maxPlayers;
    private final int mazeRows;
    private final int mazeCols;
    private final int numberOfTreasuresPerPlayer;

    public CreateGameRequest(RoutingContext ctx) {
        super(ctx);
        JsonObject json = ctx.body().asJsonObject();
        prefix = json.getString("prefix");
        gameName = new SafeString(json.getString("gameName"));
        playerName = new SafeString(json.getString("playerName"));
        gameMode = GameMode.gameModes.get(json.getString("gameMode"));
        maxPlayers = json.getInteger("maximumPlayers");

        Integer tempCols;
        Integer tempRows;
        tempRows = json.getInteger("mazeRows");
        tempCols = json.getInteger("mazeCols");
        if (tempRows == null) tempRows = 7;
        if (tempCols == null) tempCols = 7;

        mazeRows = tempRows;
        mazeCols = tempCols;

        numberOfTreasuresPerPlayer = json.getInteger("numberOfTreasuresPerPlayer");
    }

    public String getPrefix() {
        return prefix;
    }

    public SafeString getGameName() {
        return gameName;
    }

    public SafeString getPlayerName() {
        return playerName;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMazeRows() {
        return mazeRows;
    }

    public int getMazeCols() {
        return mazeCols;
    }

    public int getNumberOfTreasuresPerPlayer() {
        return numberOfTreasuresPerPlayer;
    }
}
