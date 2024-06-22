package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class GetReachableLocationsRequest extends ContextBasedRequestView {
    public GetReachableLocationsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public int getRow() {
        return params.pathParameter("row").getInteger();
    }

    public int getCol() {
        return params.pathParameter("col").getInteger();
    }
}
