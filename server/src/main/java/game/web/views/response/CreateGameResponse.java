package game.web.views.response;

import game.logic.util.SafeString;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class CreateGameResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final String gameId;
    @JsonProperty
    private final String playerName;
    @JsonProperty
    private final String playerToken;

    public CreateGameResponse(int status, String prefix, SafeString gameName, SafeString playerName, String playerToken) {
        super(status);
        this.gameId = String.format("%s_%s", prefix, gameName);
        this.playerName = playerName.toString();
        this.playerToken = playerToken;
    }
}
