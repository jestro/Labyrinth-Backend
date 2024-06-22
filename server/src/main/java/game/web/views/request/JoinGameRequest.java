package game.web.views.request;

import game.logic.util.SafeString;
import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class JoinGameRequest extends ContextBasedRequestView {
    public JoinGameRequest(RoutingContext ctx) {
        super(ctx);
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public SafeString getPlayerName() {
        return new SafeString(params.pathParameter("playerName").getString());
    }
}
