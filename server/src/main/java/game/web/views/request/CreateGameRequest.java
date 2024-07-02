package game.web.views.request;

import game.logic.GameMode;
import game.logic.util.SafeString;
import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CreateGameRequest extends ContextBasedRequestView {
    private final JsonObject json;

    public CreateGameRequest(RoutingContext ctx) {
        super(ctx);
        json = ctx.body().asJsonObject();
    }

    public String getPrefix() {
        return json.getString("prefix");
    }

    public SafeString getGameName() {
        return new SafeString(json.getString("gameName"));
    }

    public SafeString getPlayerName() {
        return new SafeString(json.getString("playerName"));
    }

    public GameMode getGameMode() {
        return GameMode.gameModes.get(json.getString("gameMode"));
    }

    public int getMaxPlayers() {
        return json.getInteger("maximumPlayers");
    }

    public int getMazeRows() {
        Integer tempRows = json.getInteger("mazeRows");
        if (tempRows == null) tempRows = 7;
        return tempRows;
    }

    public int getMazeCols() {
        Integer tempCols = json.getInteger("mazeCols");
        if (tempCols == null) tempCols = 7;
        return tempCols;
    }

    public int getNumberOfTreasuresPerPlayer() {
        return json.getInteger("numberOfTreasuresPerPlayer");
    }
}
