package game.web.views.response;

import game.logic.Game;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

import java.util.List;

public class GetGamesResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final List<Game> games;

    public GetGamesResponse(List<Game> games) {
        super(200);
        this.games = games;
    }
}
