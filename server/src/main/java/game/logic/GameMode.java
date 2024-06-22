package game.logic;

import java.util.HashMap;
import java.util.Map;

public enum GameMode {
    SIMPLE("simple"),
    TELEPORT("teleport"),
    HARDCORE("hardcore");

    public static final Map<String, GameMode> gameModes = new HashMap<>();

    private final String value;

    GameMode(String value) {
        this.value = value;
    }

    static {
        for (GameMode gameMode : GameMode.values()) {
            gameModes.put(gameMode.getValue(), gameMode);
        }
    }

    public String getValue() {
        return value;
    }
}
