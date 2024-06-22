package game.web.views.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

import java.util.List;

public class TreasureListResponse extends ResponseWithHiddenStatus {
    /**
     * -@JsonProperty makes it get automatically added to the response body.

     * Added these @SuppressWarnings because field is required for the JsonProperty.
     * Otherwise, this will cause unnecessary warnings.
     * This is because we need the field but the code inspection does not know that we need it.

     * We can also suppress all waring in a class by typing it right above the class name.
     * This might be a temporary comment but please only use suppression where applicable :)
     */
    @JsonProperty @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final List<String> treasures;

    /**
     * Only call this in LabyrinthOpenApiBridge @Operation
     */
    public TreasureListResponse(List<String> treasures) {
        super(200);
        this.treasures = treasures;
    }
}
