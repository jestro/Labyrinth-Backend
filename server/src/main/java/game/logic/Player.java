package game.logic;

import game.logic.util.SafeString;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.ext.auth.impl.UserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends UserImpl {
    private final SafeString name;
    private Position location;
    private final List<String> treasuresFound;
    private String objective;
    private PlayerState playerState;

    public Player(SafeString name) {
        this(name, null);
    }

    public Player(SafeString name, Position location) {
        treasuresFound = new ArrayList<>();
        this.location = location;
        this.name = name;
    }

    @JsonProperty()
    public String getName() {
        return name.toString();
    }

    @JsonProperty("treasuresFound")
    public List<String> getTreasuresFound() {
        return treasuresFound;
    }

    @JsonProperty()
    public String getObjective() {
        return objective;
    }

    @JsonProperty()
    public Position getLocation() {
        return location;
    }

    @JsonProperty("state")
    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(location, player.location) && Objects.equals(treasuresFound, player.treasuresFound) && Objects.equals(objective, player.objective) && playerState == player.playerState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, location, treasuresFound, objective, playerState);
    }
}
