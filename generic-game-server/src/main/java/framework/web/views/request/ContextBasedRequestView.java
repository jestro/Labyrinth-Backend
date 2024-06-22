package framework.web.views.request;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

public abstract class ContextBasedRequestView {

    protected final RoutingContext ctx;
    protected final RequestParameters params;
    protected final String playerToken;

    public ContextBasedRequestView(RoutingContext ctx) {
        this.ctx = ctx;
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        
        String token = null;
        try {
            String authHeader = ctx.request().getHeader("Authorization");
            if (authHeader == null){
                throw new IllegalArgumentException("No Authorization header found");
            }
            token = authHeader.split(" ")[1];
        } catch (Exception e){
            token = null;
        }

        this.playerToken = token;
    }

    public String getPlayerToken() {
        return this.playerToken;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getUser() {
        return (T) ctx.user();
    }
}
