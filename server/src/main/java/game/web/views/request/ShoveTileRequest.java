package game.web.views.request;

import game.logic.Tile;
import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ShoveTileRequest extends ContextBasedRequestView {
    private final JsonObject json;

    public ShoveTileRequest(RoutingContext ctx) {
        super(ctx);
        json = ctx.body().asJsonObject();
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public int getRow() {
        return json.getJsonObject("destination").getInteger("row");
    }

    public int getCol() {
        return json.getJsonObject("destination").getInteger("col");
    }

    public Tile getTile() {
        boolean[] walls = new boolean[4];
        JsonArray jsonWalls = json.getJsonObject("tile").getJsonArray("walls");
        for (int i = 0; i < walls.length; i++) {
            walls[i] = jsonWalls.getBoolean(i);
        }
        return new Tile(walls);
    }
}
