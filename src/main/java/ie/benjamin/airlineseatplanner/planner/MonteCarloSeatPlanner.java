package ie.benjamin.airlineseatplanner.planner;

import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.model.output.ResultsModel;
import ie.benjamin.airlineseatplanner.model.output.SeatsRowModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;


/**
 * Wanted to see if how'd a dead simple "AI" would handle against the waited logic solution so I impended this
 * Monte Carlo based decision model where we're only focusing on the calculated maximize customer satisfaction 💯,
 * seating all passenger groups at random!
 *
 * Results always resulted in as good as the original implementation with similar or better performance! 🤠
 */
@Slf4j
public class MonteCarloSeatPlanner extends SeatPlanner {

    final int MAX_EPOCHS;

    public MonteCarloSeatPlanner(PlaneModel planeModel) {
        super(planeModel);
        //  10 * the plane seating dimensions seems to be a grand epoch range!
        this.MAX_EPOCHS = 10*planeModel.getRowHeight() * planeModel.getRowSize();
    }

    @Override
    public ResultsModel resolve() {
        if (checkIfNotOverBooked(planeModel)) {
            return computeMontiCarloResults();
        } else {
            log.error("Looks like the plane was over booked 😥 plane size: {}, passengers: {}",
                    planeModel.getMaxSeatingSize(), planeModel.getPassengerCount());
        }
        return ResultsModel.builder().rows(rows).build();
    }


    private ResultsModel computeMontiCarloResults() {
        ResultsModel bestResults = null;
        for (int i = 0; i < MAX_EPOCHS; i++) {
            PlaneModel tmpModel =
                    PlaneModel.builder().groupList(planeModel.getGroupList())
                            .rowHeight(planeModel.getRowHeight())
                            .rowSize(planeModel.getRowSize())
                            .build();
            List<SeatsRowModel> tmpRows = createRowMap();
            for (GroupModel groupModel : tmpModel.getGroupList()) {
                if (!ranAddGroup(tmpRows, groupModel)) {
                    ranAddSingle(tmpRows, groupModel);
                }
            }
            ResultsModel tmpResults = ResultsModel.builder().rows(tmpRows).build();
            if (bestResults == null) {
                bestResults = tmpResults;
            }
            if (tmpResults.getPercentageOfSatisfiedPassengers() >= bestResults.getPercentageOfSatisfiedPassengers()) {
                bestResults = tmpResults;
                if (bestResults.getPercentageOfSatisfiedPassengers() >= 100d) {
                    return bestResults;
                }
            }
        }
        return bestResults;
    }

    private void orderGroupsWindowPref(GroupModel groupModel) {
        groupModel.getPassengerList().sort((o1, o2) -> -Boolean.compare(o1.isWindowPref(), o2.isWindowPref()));
        // condition if there is 2 users in group with windowPref and same size as whole row.
        // todo if the chase a group is larger then row add splitting re ordering logic..
        if (groupModel.getPassengerList().size() > 1){
            if (groupModel.getPassengerList().get(1).isWindowPref() && planeModel.getRowSize() == groupModel.getPassengerList().size()){
                groupModel.getPassengerList().add((groupModel.getPassengerList().remove(1)));
            }
        }
    }

    private boolean ranAddGroup(List<SeatsRowModel> tmpRows, GroupModel groupModel) {
        for (int i = 0; i < tmpRows.size() * 2; i++) {
            SeatsRowModel row = tmpRows.get(new Random().nextInt(tmpRows.size()));
            if (!row.isFull()) {
                if (row.getRowSize() >= (row.getFilledSeats() + groupModel.getGroupSize())) {
                    orderGroupsWindowPref(groupModel);
                    row.add(groupModel);
                    return true;
                }
            }
        }
        return false;
    }

    private void ranAddSingle(List<SeatsRowModel> tmpRows, GroupModel groupModel) {
        for (PassengerModel passenger : groupModel.getPassengerList()) {
            while (true) {
                SeatsRowModel row = tmpRows.get(new Random().nextInt(tmpRows.size()));
                if (!row.isFull()) {
                    row.add(passenger);
                    break;
                }
            }
        }
    }
}
