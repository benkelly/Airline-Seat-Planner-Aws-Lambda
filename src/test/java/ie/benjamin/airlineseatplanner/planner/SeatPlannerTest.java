package ie.benjamin.airlineseatplanner.planner;

import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.model.output.ResultsModel;
import ie.benjamin.airlineseatplanner.model.output.SeatsRowModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class SeatPlannerTest {
    @Mock
    PlaneModel planeModel;
    @Mock
    List<SeatsRowModel> rows;
    @Mock
    Logger log;
    @InjectMocks
    SeatPlanner seatPlanner;

    private GroupModel testGroup;
    private List<GroupModel> testGroupList;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        List<PassengerModel> passengerModelList = new ArrayList<>();
        passengerModelList.add(passengerModel);
        testGroup = GroupModel.builder().passengerList(passengerModelList).build();
        testGroupList = new ArrayList<>();
        testGroupList.add(testGroup);
    }


    @Test
    void testResolve() {
        when(planeModel.getMaxSeatingSize()).thenReturn(0);
        when(planeModel.getPassengerCount()).thenReturn(0);
        when(planeModel.getGroupList()).thenReturn(Arrays.asList(testGroup));

        ResultsModel result = seatPlanner.resolve();
        ResultsModel resultsModel = ResultsModel.builder().rows(new ArrayList<>()).build();
        Assertions.assertEquals(resultsModel, result);
    }

    @Test
    void testAssignGroups() {
        when(planeModel.getGroupList()).thenReturn(Arrays.asList(testGroup));

        seatPlanner.assignGroups();
    }

    @Test
    void testAssignGroupsToRowsNull() {
        seatPlanner.assignGroupsToRows(null);
    }

    @Test
    void testAssignGroupsToRows() {
        seatPlanner.assignGroupsToRows(testGroup);
    }

    @Test
    void testAssignSinglePassengerToSeatsNull() {
        seatPlanner.assignPassengerToSeat(null);
    }

    @Test
    void testAssignSinglePassengerToSeats() {
        seatPlanner.assignPassengerToSeat(testGroup);
    }

    @Test
    void testCheckIfNotOverBooked() {
        when(planeModel.getMaxSeatingSize()).thenReturn(0);
        when(planeModel.getPassengerCount()).thenReturn(0);

        boolean result = seatPlanner.checkIfNotOverBooked(planeModel);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCreateRowMap() {
        when(planeModel.getRowSize()).thenReturn(0);
        when(planeModel.getRowHeight()).thenReturn(0);

        List<SeatsRowModel> result = seatPlanner.createRowMap();
        Assertions.assertEquals("[]", result.toString());
    }
}