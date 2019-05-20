package ie.benjamin.airlineseatplanner.planner;


import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.model.output.ResultsModel;
import ie.benjamin.airlineseatplanner.model.output.SeatsRowModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Aims to satisfy the most important passengers first (i.e. the largest groups with Window
 * prefs), by sorting their seats first and descending thought the passenger groups in that pattern. All while
 * comparing the possible satisfaction for each possible row based on if that current row will satisfy all their needs
 * factoring the the already seated passengers in window seat and such.
 */
@Slf4j
public class SeatPlanner {

    final PlaneModel planeModel;
    List<SeatsRowModel> rows;

    public SeatPlanner(PlaneModel planeModel) {
        this.planeModel = planeModel;
        this.rows = createRowMap();
    }

    /**
     * @return ResultsModel of the resolved seating plan.
     */
    public ResultsModel resolve() {
        if (checkIfNotOverBooked(planeModel)) {
            assignGroups();
        } else {
            log.error("Looks like the plane was over booked 😥 plane size: {}, passengers: {}",
                    planeModel.getMaxSeatingSize(), planeModel.getPassengerCount());
        }
        return ResultsModel.builder().rows(rows).build();
    }


    /**
     * Orders groups based on size and number of window prefs
     */
    void assignGroups() {
        List<GroupModel> groups = planeModel.getGroupList();
        groups.sort((o1, o2) -> -(o1.getGroupScore()).compareTo(o2.getGroupScore()));
        groups.forEach(this::assignGroupsToRows);
    }


    /**
     * @param group Assigns Group row of highest Group Satisfaction based on each ot the possible rows factors.
     *              if unable to assign group -> then slip up.
     */
    void assignGroupsToRows(GroupModel group) {
        Optional<SeatsRowModel> maxRow = Optional.empty();
        Optional<Double> maxGroupSatisfaction = Optional.empty();

        for (SeatsRowModel row : rows) {
            Optional<Double> potentialGroupSatisfaction = row.getPotentialGroupSatisfaction(group);
            if (potentialGroupSatisfaction.isPresent()) {
                if (!maxRow.isPresent() || potentialGroupSatisfaction.get() > maxGroupSatisfaction.get()) {
                    maxGroupSatisfaction = potentialGroupSatisfaction;
                    maxRow = Optional.of(row);
                }
            }
        }
        if (maxRow.isPresent()) {
            maxRow.get().add(group);
        } else {
            assignPassengerToSeat(group);
        }
    }

    /**
     * @param groupModel To be split into random rows
     */
    void assignPassengerToSeat(GroupModel groupModel) {
        if (groupModel != null) {
            for (PassengerModel passenger : groupModel.getPassengerList()) {
                for (SeatsRowModel row : rows) {
                    if (!row.isFull()) {
                        row.add(passenger);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param planeModel of the current plane model to be resoled
     * @return boolean true - if the passenger list is <= then plane's seats.
     */
    boolean checkIfNotOverBooked(PlaneModel planeModel) {
        return planeModel.getPassengerCount() <= planeModel.getMaxSeatingSize();
    }

    /**
     * @return List<SeatsRowModel> a seatRowModel of the plane model to be used in this current Planning job.
     */
    List<SeatsRowModel> createRowMap() {
        List<SeatsRowModel> rows = new ArrayList<>();
        for (int i = 0; i < planeModel.getRowHeight(); i++) {
            rows.add(new SeatsRowModel(planeModel.getRowSize()));
        }
        return rows;
    }
}
