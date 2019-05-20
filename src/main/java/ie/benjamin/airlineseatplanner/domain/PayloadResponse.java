package ie.benjamin.airlineseatplanner.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadResponse {

    private String message;

    public PayloadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
