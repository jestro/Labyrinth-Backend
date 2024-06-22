package game.web.views.response;


import game.logic.Game;
import game.logic.Maze;
import game.logic.Player;
import game.logic.Tile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGameDetailsResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final Game description;

    @JsonProperty
    private final Map<String, Player> players;

    @JsonProperty
    private final Maze maze;

    @JsonProperty
    private final Tile spareTile;

    public GetGameDetailsResponse(Game game, Map<String, Player> players, Maze maze, Tile spareTile) {
        super(200);
        this.description = game;
        this.players = players;
        this.maze = maze;
        this.spareTile = spareTile;
    }

}
