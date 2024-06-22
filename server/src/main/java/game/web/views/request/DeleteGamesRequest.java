package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class DeleteGamesRequest extends ContextBasedRequestView {
    public DeleteGamesRequest(RoutingContext ctx) {
        super(ctx);
    }
}
