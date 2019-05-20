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
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class ResultsModelTest {
//    @Mock
//    List<SeatsRowModel> rows;
//    @Mock
//    Logger log;
//    @InjectMocks
    ResultsModel resultsModel;

    List<SeatsRowModel> seatsRowModelList;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.initMocks(this);
        PassengerModel passengerModel = PassengerModel.builder().name("name")
                .windowPref(false).build();
        SeatsRowModel seatsRowModel = new SeatsRowModel(1);
        seatsRowModel.add(passengerModel);
        seatsRowModelList = new ArrayList<>();
        seatsRowModelList.add(seatsRowModel);

        resultsModel = ResultsModel.builder().rows(seatsRowModelList).build();
    }

    @Test
    void testPrint() {
        String result = resultsModel.print();
        Assert.assertThat(result, containsString("name"));
        Assert.assertThat(result, containsString("0%"));
        resultsModel = ResultsModel.builder().build();
        Assertions.assertEquals("\n", resultsModel.print());
    }

    @Test
    void testGetNumberOfSatisfiedPassengers() {
        int result = resultsModel.getNumberOfSatisfiedPassengers();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetNumberOfPassengersSeated() {
        int result = resultsModel.getNumberOfPassengersSeated();
        Assertions.assertEquals(1, result);
    }

    @Test
    void testGetPercentageOfSatisfiedPassengers() {
        Assertions.assertEquals(0d, resultsModel.getPercentageOfSatisfiedPassengers());
    }

    @Test
    void testBuilder() {
        ResultsModel.ResultsModelBuilder result = ResultsModel.builder();
        Assertions.assertNotEquals(new ResultsModel.ResultsModelBuilder(), result);
    }

    @Test
    void testEquals() {
        boolean result = resultsModel.equals(new ResultsModel.ResultsModelBuilder().build());
        Assertions.assertEquals(false, result);
        resultsModel = ResultsModel.builder().rows(seatsRowModelList).build();

        Assertions.assertEquals(true, resultsModel.equals(new ResultsModel.ResultsModelBuilder()
                .rows(seatsRowModelList)
                .build()));


    }

    @Test
    void testCanEqual() {
        boolean result = resultsModel.canEqual(new ResultsModel.ResultsModelBuilder().build());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHashCode() {
        int result = resultsModel.hashCode();
        Assertions.assertNotEquals(new ResultsModel.ResultsModelBuilder().build().hashCode(), result);
    }

    @Test
    void testToString() {
        String result = resultsModel.toString();
        Assert.assertThat(result, containsString("ResultsModel"));
        Assert.assertThat(result, containsString("rows"));
    }
}