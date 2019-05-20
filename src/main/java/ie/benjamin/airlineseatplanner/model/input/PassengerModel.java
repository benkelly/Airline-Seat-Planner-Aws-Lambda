package ie.benjamin.airlineseatplanner.model.input;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@JsonDeserialize(builder = PassengerModel.PassengerModelBuilder.class)
public class PassengerModel {

    @Getter
    private boolean windowPref;
    @Getter
    private String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PassengerModelBuilder {
    }
}
