package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class MovePlayerRequest extends ContextBasedRequestView {
    private final JsonObject json;

    public MovePlayerRequest(RoutingContext ctx) {
        super(ctx);
        json = ctx.body().asJsonObject();
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public String getPlayerName() {
        return params.pathParameter("playerName").getString();
    }

    public int getRow() {
        return json.getJsonObject("destination").getInteger("row");
    }

    public int getCol() {
        return json.getJsonObject("destination").getInteger("col");
    }
}
