package game.web.views.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import game.logic.Position;
import framework.web.views.response.ResponseWithHiddenStatus;

public class GetReachableLocationsResponse extends ResponseWithHiddenStatus {
    @JsonProperty("reachable")
    private final List<Position> reachable;

    public GetReachableLocationsResponse(List<Position> reachableLocations) {
        super(200);
        this.reachable = reachableLocations;
    }
}
