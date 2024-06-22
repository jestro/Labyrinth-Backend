package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class MovePlayerRequest extends ContextBasedRequestView {

    private final int row;
    private final int col;

    public MovePlayerRequest(RoutingContext ctx) {
        super(ctx);
        JsonObject json  = ctx.body().asJsonObject();
        row = json.getJsonObject("destination").getInteger("row");
        col = json.getJsonObject("destination").getInteger("col");
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public String getPlayerName() {
        return params.pathParameter("playerName").getString();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
