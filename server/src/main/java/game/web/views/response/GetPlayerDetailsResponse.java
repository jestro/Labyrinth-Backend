package game.web.views.response;

import game.logic.Player;
import game.logic.Position;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

import java.util.List;

public class GetPlayerDetailsResponse extends ResponseWithHiddenStatus {
    @JsonProperty("name")
    private final String playerName;

    @JsonProperty("treasuresFound")
    private final List<String> treasuresFound;

    @JsonProperty("location")
    private final Position location;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("objective")
    private final String objective;

    public GetPlayerDetailsResponse(Player player) {
        super(200);
        this.playerName = player.getName();
        this.location = player.getLocation();
        this.status = player.getPlayerState().toString();
        this.objective = player.getObjective();
        this.treasuresFound = player.getTreasuresFound();
    }
}
