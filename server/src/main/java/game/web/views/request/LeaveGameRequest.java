package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class LeaveGameRequest extends ContextBasedRequestView {
    public LeaveGameRequest(RoutingContext ctx) {
        super(ctx);
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public String getPlayerName() {
        return params.pathParameter("playerName").getString();
    }
}
