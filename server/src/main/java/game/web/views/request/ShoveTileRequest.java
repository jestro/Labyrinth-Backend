package game.web.views.request;

import game.logic.Tile;
import framework.web.views.request.ContextBasedRequestView;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ShoveTileRequest extends ContextBasedRequestView {

    private final int row;
    private final int col;
    private final Tile tile;

    public ShoveTileRequest(RoutingContext ctx) {
        super(ctx);
        JsonObject json  = ctx.body().asJsonObject();
        row = json.getJsonObject("destination").getInteger("row");
        col = json.getJsonObject("destination").getInteger("col");

        boolean[] walls = new boolean[4];
        JsonArray jsonWalls = json.getJsonObject("tile").getJsonArray("walls");
        for (int i = 0; i < walls.length; i++) {
            walls[i] = jsonWalls.getBoolean(i);
        }
        tile = new Tile(walls);
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Tile getTile() {
        return tile;
    }
}
