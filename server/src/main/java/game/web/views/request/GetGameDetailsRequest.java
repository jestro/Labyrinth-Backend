package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class GetGameDetailsRequest extends ContextBasedRequestView {
    public GetGameDetailsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public boolean showDescription() {
        return params.queryParameter("description").getBoolean();
    }

    public boolean showPlayers() {
        return params.queryParameter("players").getBoolean();
    }

    public boolean showMaze() {
        return params.queryParameter("maze").getBoolean();
    }

    public boolean showSpareTile() {
        return params.queryParameter("spareTile").getBoolean();
    }
}
