package ie.benjamin.airlineseatplanner.model.output;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@Builder
@JsonDeserialize(builder = ResultsModel.ResultsModelBuilder.class)
public class ResultsModel {
    private final List<SeatsRowModel> rows;


    public int getNumberOfSatisfiedPassengers() {
        return rows.stream().mapToInt(SeatsRowModel::getNumberOfSatisfiedPassengers).sum();
    }

    public int getNumberOfPassengersSeated() {
        return rows.stream().mapToInt(SeatsRowModel::getFilledSeats).sum();
    }
    public double getPercentageOfSatisfiedPassengers() {
        return 100.0 * getNumberOfSatisfiedPassengers() / getNumberOfPassengersSeated();
    }

    public String print() {
        StringBuilder stringBuilder = new StringBuilder("\n");
        if (rows != null) {
            if (rows.size() > 0) {
                for (SeatsRowModel row : rows) {
                    stringBuilder.append(row.resultsToString()+"\n");
                }
                stringBuilder.append(String.format("%.0f%%", getPercentageOfSatisfiedPassengers()));
                log.info(stringBuilder.toString());
            }
        }
        return String.valueOf(stringBuilder);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ResultsModelBuilder {}
}
