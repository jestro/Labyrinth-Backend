package game.web.views.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class MessageResponse extends ResponseWithHiddenStatus {

    @JsonProperty @SuppressWarnings({"unused", "FieldCanBeLocal"})
     private final String message;
    public MessageResponse(int status, String message) {
        super(status);
        this.message = message;
    }
}
