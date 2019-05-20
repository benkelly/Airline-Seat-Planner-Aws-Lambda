package ie.benjamin.airlineseatplanner.model.output;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import lombok.Data;

import java.util.*;
import java.util.stream.Collector;


@Data
public class SeatsRowModel {

    private int rowSize;
    @JsonIgnore
    private int splitGroupPassenger = 0;
    private List<GroupModel> assignedList;


    public SeatsRowModel(int rowSize) {
        this.rowSize = rowSize;
        this.assignedList = new ArrayList<>();
    }

    public void add(GroupModel group) {
        assignedList.add(group);
    }

    public int getFilledSeats() {
            return assignedList.stream()
                    .filter(groupModel -> Objects.nonNull(groupModel))
                    .map(GroupModel::getGroupSize)
                    .reduce(0, Integer::sum);
    }

    public boolean isFull() {
        return rowSize == getFilledSeats();
    }

    public void add(PassengerModel passenger) {
        splitGroupPassenger++;
        assignedList.add(GroupModel.builder().passengerList(Arrays.asList(passenger)).build());
    }


    public String resultsToString(){
                return assignedList.stream().map(GroupModel::getGroupNames).collect(Collector.of(StringBuilder::new,
                        (stringBuilder, str) -> stringBuilder.append(str).append(" "),
                        StringBuilder::append,
                        StringBuilder::toString))
                        .replaceAll("\\s\\n", "\n").trim()
                        ;
    }


    public Optional<Double> getPotentialGroupSatisfaction(GroupModel group) {
        if (group != null) {
            // if groups too large for row.
            if (group.getGroupSize() > rowSize-getFilledSeats()) {
                return Optional.empty();
            }
            //check if passengers wanting window seat in row
            int windowSeatsFilled = (int) assignedList.stream()
                    .flatMap(g -> g.getPassengerList().stream())
                    .filter(passenger -> passenger.isWindowPref())
                    .count();
            int windowSeatsGroupPref = (int) group.getPassengerList().stream()
                    .filter(passenger -> passenger.isWindowPref())
                    .count();

            // reduce potential Satisfaction by the the amount of windowPref unable to fill
            int windowSeatsDenied = Math.min(0, windowSeatsGroupPref-Math.max(0, 2-windowSeatsFilled));
            Double potentialSatisfaction = group.getMaxSatisfaction() - windowSeatsDenied;
            return Optional.of(potentialSatisfaction);
        } else {
            return Optional.empty();
        }
    }

    int getNumberOfSatisfiedPassengers() {
        if (!assignedList.isEmpty()) {
            int totalSatisfaction = 0;
            for (GroupModel group : assignedList) {
                    totalSatisfaction += group.getMaxSatisfaction();
            }
            totalSatisfaction-=splitGroupPassenger;
            int windowPreferringPassengers = (int)assignedList.stream()
                    .flatMap(group -> group.getPassengerList().stream())
                    .filter(PassengerModel::isWindowPref)
                    .count();
            return totalSatisfaction-Math.max(0, windowPreferringPassengers-2);
        }
        return 0;
    }
}
