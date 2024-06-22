package framework.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Webserver extends AbstractVerticle {
    private static final Logger LOGGER = Logger.getLogger(Webserver.class.getName());

    private final int port;
    private final String path2spec;
    private final OpenApiBridge bridge;


    public Webserver(int port, String path2spec, OpenApiBridge bridge) {
        this.port = port;
        this.path2spec = path2spec;
        this.bridge = bridge;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        buildRouter(path2spec)
                .onFailure(cause -> {
                    LOGGER.log(Level.SEVERE, "Failed to load API specification", cause);
                    startPromise.fail(cause);
                })
                .compose(this::startWebserver)
                .onFailure(startPromise::fail)
                .onSuccess(str->startPromise.complete());
    }

    private Future<HttpServer> startWebserver(Router router) {
        Router mainRouter = Router.router(vertx);
        mainRouter.route("/*")        // for all requests
                .handler(this::logRequest) // log ... and
                .subRouter(router);        // server.
        return vertx.createHttpServer()
                .requestHandler(mainRouter)
                .listen(port)
                .onFailure(cause->LOGGER.log(Level.SEVERE, "Failed to start webserver", cause))
                .onSuccess(server -> LOGGER.log(Level.INFO, ()->String.format("Server is listening on port: %d", server.actualPort())));
    }

    private Future<Router> buildRouter(String path) {
        return RouterBuilder.create(vertx, path)
                .onSuccess(rb -> LOGGER.log(Level.INFO, "Spec successfully loaded"))
                .map(bridge::buildRouter);
    }

    private void logRequest(RoutingContext ctx) {
        LOGGER.log(Level.INFO, ()->String.format("REQUEST: %s %s", ctx.request().method(), ctx.request().path()));
        ctx.next();
    }
}
