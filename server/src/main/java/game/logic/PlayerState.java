package game.logic;

public enum PlayerState {
    WAITING("WAITING"),
    PLAYING("PLAYING"),
    WON("WON"),
    LEFT("LEFT"),
    LOST("LOST");

    private final String value;

    PlayerState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}