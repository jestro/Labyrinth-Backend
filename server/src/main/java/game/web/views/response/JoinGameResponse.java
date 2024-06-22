package game.web.views.response;

import game.logic.util.SafeString;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class JoinGameResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final String gameId;
    @JsonProperty
    private final String playerName;
    @JsonProperty
    private final String playerToken;

    public JoinGameResponse(int status, String gameId, SafeString playerName, String playerToken) {
        super(status);
        this.gameId = gameId;
        this.playerName = playerName.toString();
        this.playerToken = playerToken;
    }
}
