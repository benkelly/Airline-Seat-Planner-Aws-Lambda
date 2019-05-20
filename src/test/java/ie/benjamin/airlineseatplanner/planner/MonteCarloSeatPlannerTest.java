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

class MonteCarloSeatPlannerTest {
    @Mock
    Logger log;
    @Mock
    PlaneModel planeModel;
    @Mock
    List<SeatsRowModel> rows;
    @InjectMocks
    MonteCarloSeatPlanner monteCarloSeatPlanner;

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
        when(planeModel.getMaxSeatingSize()).thenReturn(4);
        when(planeModel.getPassengerCount()).thenReturn(testGroup.getGroupSize());
        when(planeModel.getRowSize()).thenReturn(2);
        when(planeModel.getRowHeight()).thenReturn(2);
        when(planeModel.getGroupList()).thenReturn(testGroupList);

        ResultsModel result = monteCarloSeatPlanner.resolve();
        Assertions.assertEquals(null, result);
    }
    @Test
    void testResolveOverBooked() {
        when(planeModel.getMaxSeatingSize()).thenReturn(0);
        when(planeModel.getPassengerCount()).thenReturn(testGroup.getGroupSize());
        when(planeModel.getRowSize()).thenReturn(2);
        when(planeModel.getRowHeight()).thenReturn(2);
        when(planeModel.getGroupList()).thenReturn(testGroupList);

        ResultsModel result = monteCarloSeatPlanner.resolve();
        Assertions.assertEquals(true, result.getRows().isEmpty());
    }
}