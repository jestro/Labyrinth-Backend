package game.web.views.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.views.response.ResponseWithHiddenStatus;

public class FailureResponse  extends ResponseWithHiddenStatus {
    @JsonProperty
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String cause;

    @JsonProperty("failure")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final int failureCode;

    public FailureResponse(int status, String cause) {
        super(status);
        this.failureCode = status;
        this.cause = cause;
    }
}
