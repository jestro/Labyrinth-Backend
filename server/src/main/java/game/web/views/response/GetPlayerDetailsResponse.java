package game.web.views.response;

import game.logic.Player;
import game.logic.Position;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

import java.util.List;

public class GetPlayerDetailsResponse extends ResponseWithHiddenStatus {
    private final Player player;

    public GetPlayerDetailsResponse(Player player) {
        super(200);
        this.player = player;
    }

    @JsonProperty("name")
    public String getPlayerName() {
        return player.getName();
    }

    @JsonProperty("location")
    public Position getPlayerLocation() {
        return player.getLocation();
    }

    @JsonProperty("status")
    public String getPlayerStatus() {
        return player.getPlayerState().toString();
    }

    @JsonProperty("objective")
    public String getPlayerObjective() {
        return player.getObjective();
    }

    @JsonProperty("treasuresFound")
    public List<String> getPlayerTreasuresFound() {
        return player.getTreasuresFound();
    }
}
