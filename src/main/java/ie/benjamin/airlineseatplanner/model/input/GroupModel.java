package ie.benjamin.airlineseatplanner.model.input;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;

@Slf4j
@Builder
@Data
@JsonDeserialize(builder = GroupModel.GroupModelBuilder.class)
public class GroupModel {

    private final List<PassengerModel> passengerList;

    public Double getMaxSatisfaction() {
        return Double.valueOf(passengerList.size());
    }

    /**
     * @return calc group weight to compare to others
     */
    public Double getGroupScore() {
        return getMaxSatisfaction() + 0d * (int) passengerList.stream().filter(passenger -> passenger.isWindowPref()).count();
    }

    public int getGroupSize() {
        return passengerList.size();
    }

    public String getGroupNames() {
        return passengerList.stream()
                .filter(passengerModel -> Objects.nonNull(passengerModel))
                .map(PassengerModel::getName).collect(Collector.of(StringBuilder::new,
                (stringBuilder, str) -> stringBuilder.append(str).append(" "),
                StringBuilder::append,
                StringBuilder::toString))
                .replaceAll("\\s{2,}", " ").trim();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GroupModelBuilder {}

}
