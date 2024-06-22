package framework.web.views.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseWithHiddenStatus {

    private final int status;


    public ResponseWithHiddenStatus(int status) {
        this.status = status;
    }

    @JsonIgnore
    public int getStatus() {
        return status;
    }
}
