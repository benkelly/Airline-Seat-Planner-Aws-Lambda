package ie.benjamin.airlineseatplanner.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@Data
@Builder
@JsonDeserialize(builder = PlaneModel.PlaneModelBuilder.class)
public class PlaneModel {

    @Getter
    private int rowSize;

    @Getter
    private int rowHeight;

    @Setter
    @Getter
    private final List<GroupModel> groupList;

    public int getMaxSeatingSize(){
        return rowSize * rowHeight;
    }

    public int getPassengerCount() {
            return groupList.stream()
                    .filter(groupModel -> Objects.nonNull(groupModel))
                    .map(GroupModel::getGroupSize)
                    .reduce(0, Integer::sum);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class PlaneModelBuilder {}

}
