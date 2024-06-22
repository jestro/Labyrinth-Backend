package game.web.views.response;

import game.logic.Player;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class MoveResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final Player player;

    public MoveResponse(Player player) {
        super(200);
        this.player = player;
    }
}
