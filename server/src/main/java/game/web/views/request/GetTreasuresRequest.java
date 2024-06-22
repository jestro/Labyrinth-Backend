package game.web.views.request;

import framework.web.views.request.ContextBasedRequestView;
import io.vertx.ext.web.RoutingContext;

public class GetTreasuresRequest extends ContextBasedRequestView {
    /***
     * @param ctx no additions needed here; the request does not have any options
     */
    public GetTreasuresRequest(RoutingContext ctx) {
        super(ctx);
    }

}
