package ie.benjamin.airlineseatplanner.model.output;

import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class SeatsRowModelTest {

    SeatsRowModel seatsRowModel;

    private GroupModel testGroup;
    @BeforeEach
    void setUp() {
        seatsRowModel = new SeatsRowModel(4);

        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        List<PassengerModel> passengerModelList = new ArrayList<>();
        passengerModelList.add(passengerModel);
        testGroup = GroupModel.builder().passengerList(passengerModelList).build();
    }

    @Test
    void testGetFilledSeats() {
        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        int result = seatsRowModel.getFilledSeats();
        Assertions.assertEquals(4, result);
    }

    @Test
    void testIsFull() {
        Assertions.assertEquals(false, seatsRowModel.isFull());
        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        seatsRowModel.add(passengerModel);
        Assertions.assertEquals(true, seatsRowModel.isFull());

    }

    @Test
    void testAddPassengerModel() {
        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        seatsRowModel.add(passengerModel);
    }

    @Test
    void testAddGroupModel() {
        GroupModel groupModel = null;
        seatsRowModel.add(groupModel);
        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        List<PassengerModel> passengerModelList = new ArrayList<>();
        passengerModelList.add(passengerModel);
        groupModel = GroupModel.builder().passengerList(passengerModelList).build();
        seatsRowModel.add(groupModel);
    }

    @Test
    void testResultsToString() {
        seatsRowModel.add(testGroup);
        String result = seatsRowModel.resultsToString();
        Assert.assertThat(result, containsString("name"));

    }

    @Test
    void testGetPotentialGroupSatisfaction() {
        Optional<Double> result = seatsRowModel.getPotentialGroupSatisfaction(null);
        Assertions.assertEquals(Optional.empty(), result);
        result = seatsRowModel.getPotentialGroupSatisfaction(testGroup);
        Assertions.assertEquals(Optional.of(3.0), result);
    }

    @Test
    void testGetNumberOfSatisfiedPassengers() {
        int result = seatsRowModel.getNumberOfSatisfiedPassengers();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testSetRowSize() {
        seatsRowModel.setRowSize(0);
    }

    @Test
    void testSetSplitGroupPassenger() {
        seatsRowModel.setSplitGroupPassenger(0);
    }

    @Test
    void testSetAssignedList() {
        seatsRowModel.setAssignedList(Arrays.asList(testGroup));
    }

    @Test
    void testEquals() {
        SeatsRowModel tempSeatsRowModel = new SeatsRowModel(0);
        boolean result = seatsRowModel.equals(tempSeatsRowModel);
        Assertions.assertEquals(false, result);
        tempSeatsRowModel = new SeatsRowModel(4);
        result = seatsRowModel.equals(tempSeatsRowModel);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCanEqual() {
        SeatsRowModel tempSeatsRowModel = new SeatsRowModel(0);
        boolean result = seatsRowModel.canEqual(tempSeatsRowModel);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHashCode() {
        int result = seatsRowModel.hashCode();
        SeatsRowModel tempSeatsRowModel = seatsRowModel;
        Assertions.assertEquals(tempSeatsRowModel.hashCode(), result);
    }

    @Test
    void testToString() {
        String result = seatsRowModel.toString();
        Assert.assertThat(result, containsString("rowSize"));
    }
}