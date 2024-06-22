package game;

import framework.util.Config;
import game.web.LabyrinthOpenApiBridge;
import framework.web.OpenApiBridge;
import framework.web.Webserver;
import io.vertx.core.AbstractVerticle;

@SuppressWarnings("unused") // This class is called as "main verticle" by the vertx launcher
public class StartUp extends AbstractVerticle {

    private final String path2spec;
    private final OpenApiBridge bridge;


    public StartUp(String path2spec, OpenApiBridge bridge) {
        this.path2spec = path2spec;
        this.bridge = bridge;
    }

    public StartUp() {
        this(
                Config.getString("spec.url"),
                new LabyrinthOpenApiBridge()
        );
    }

    @Override
    public void start() {
        vertx.deployVerticle(new Webserver(Config.getInteger("server.port"),
                path2spec,
                bridge));
    }

}
