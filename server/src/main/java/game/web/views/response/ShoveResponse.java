package game.web.views.response;

import game.logic.Maze;
import game.logic.Tile;
import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class ShoveResponse extends ResponseWithHiddenStatus {
    @JsonProperty
    private final Maze maze;

    @JsonProperty
    private final Tile spareTile;

    public ShoveResponse(Maze maze, Tile spareTile) {
        super(200);
        this.maze = maze;
        this.spareTile = spareTile;
    }



}
