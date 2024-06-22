package framework.web;

import framework.logic.GameResourceNotFoundException;
import framework.logic.GameRuleException;
import framework.web.views.response.ResponseWithHiddenStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.BadRequestException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class OpenApiBridge {

    private static final Logger LOGGER = Logger.getLogger(OpenApiBridge.class.getName());

    private final Map<String, AuthenticationHandler> securityHandlers = new HashMap<>();

    protected void setSecurityHandlers(Map<String, AuthenticationHandler> handlers) {
        this.securityHandlers.putAll(handlers);
    }

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.FINE, "Installing CORS handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.FINE, "Installing body-handler handler");
        routerBuilder.rootHandler(BodyHandler.create());

        LOGGER.log(Level.FINE, "Installing security handlers");
        securityHandlers.forEach(routerBuilder::securityHandler);

        LOGGER.log(Level.FINE, "Installing General Failure handlers");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.FINE, "Installing API-Handlers handlers");
        routerBuilder.operations().forEach(this::installHandlers);


        LOGGER.log(Level.INFO, "All handlers are installed");
        return routerBuilder.createRouter();
    }

    private void installHandlers(io.vertx.ext.web.openapi.Operation operation) {
        Stream.of(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Operation.class))
                .filter(method -> method.getAnnotation(Operation.class).value().equals(operation.getOperationId()))
                .map(this::method2handler)
                .forEach(operation::handler);
    }

    private Handler<RoutingContext> method2handler(Method method) {
        Function<RoutingContext,?> mapper;
        try {
            mapper = createMapper(method);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "Operation handler method has unexpected signature (argument)", e);
            return ctx -> ctx.fail(500);
        }

        if (!ResponseWithHiddenStatus.class.isAssignableFrom(method.getReturnType())){
            LOGGER.log(Level.SEVERE, ()->"Operation handler method has unexpected signature (return type): " + method.getReturnType());
            return ctx -> ctx.fail(500);
        }

        return ctx -> {
            try {
                Object wrappedCtx = mapper.apply(ctx);
                LOGGER.log(Level.INFO, ()->String.format("Calling method %s with as arg %s", method.getName(),wrappedCtx.getClass().getName()));
                ResponseWithHiddenStatus responseObject = (ResponseWithHiddenStatus) method.invoke(this, wrappedCtx);
                sendJson(ctx, responseObject.getStatus(), responseObject);
            } catch (IllegalAccessException e) {
                ctx.fail(e);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException){
                    throw (RuntimeException) e.getCause();
                } else {
                    ctx.fail(e);
                }
            }
        };
    }

    private Function<RoutingContext,?> createMapper(Method method) {
        Class<?> expectedType = method.getParameterTypes()[0];
        if (expectedType.equals(RoutingContext.class)) return ctx -> ctx;

        try {
            Constructor<?> constructor = expectedType.getConstructor(RoutingContext.class);
            return ctx -> {
                try {
                    return constructor.newInstance(ctx);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e); // NOSONAR this is the most generic unchecked exception: for use in a lambda
                }
            };
        } catch (NoSuchMethodException | SecurityException |ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("Operation handler method has unexpected parameter type", e);
        }
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.

        if (cause instanceof BadRequestException) {
            // throw new RequestPredicateException("message", cause); when you manually found some wrong data in a request
            code = ctx.statusCode(); // code should be 400
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof ForbiddenAccessException) {
            code = 403;
        } else if (cause instanceof GameResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof GameRuleException) {
            code = 409;
        } else {
            LOGGER.log(Level.SEVERE, "Unanticipated Failed request: cause", cause);
        }

        sendFailure(ctx, code, quote);
    }

    protected CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }

    protected void sendJson(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    protected void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJson(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }
}
