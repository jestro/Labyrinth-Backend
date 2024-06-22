package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class GetGamesRequest extends ContextBasedRequestView {
    public GetGamesRequest(RoutingContext ctx) {
        super(ctx);
    }

    public boolean onlyAccepting() {
        return params.queryParameter("onlyAccepting").getBoolean();
    }

    public String getPrefix() {
        return params.queryParameter("prefix").getString();
    }
}
