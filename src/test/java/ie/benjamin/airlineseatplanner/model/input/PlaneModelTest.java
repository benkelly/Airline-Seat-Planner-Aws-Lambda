package ie.benjamin.airlineseatplanner.model.input;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class PlaneModelTest {

    PlaneModel planeModel;

    @BeforeEach
    void setUp() {
        planeModel = PlaneModel.builder().build();
    }

    @Test
    void testGetMaxSeatingSize() {
        int result = planeModel.getMaxSeatingSize();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetPassengerCount() {
        List<GroupModel> groupModels = new ArrayList<>();
        List<PassengerModel> passengerModels = new ArrayList<>();
        passengerModels.add(new PassengerModel.PassengerModelBuilder()
                .name("name")
                .windowPref(false).build());
        groupModels.add(new GroupModel.GroupModelBuilder().passengerList(passengerModels).build());
        planeModel = PlaneModel.builder().groupList(groupModels).build();
        Assertions.assertEquals(1, planeModel.getPassengerCount());


    }

    @Test
    void testBuilder() {
        PlaneModel.PlaneModelBuilder result = PlaneModel.builder();
        Assertions.assertNotEquals(new PlaneModel.PlaneModelBuilder(), result);
    }

    @Test
    void testSetRowSize() {
        planeModel.setRowSize(0);
    }

    @Test
    void testSetRowHeight() {
        planeModel.setRowHeight(0);
    }

    @Test
    void testEquals() {
        boolean result = planeModel.equals(new PlaneModel.PlaneModelBuilder().build());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCanEqual() {
        boolean result = planeModel.canEqual(new PlaneModel.PlaneModelBuilder().build());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHashCode() {
        int result = planeModel.hashCode();
        Assertions.assertEquals(new PlaneModel.PlaneModelBuilder().build().hashCode(), result);
    }

    @Test
    void testToString() {
        String result = planeModel.toString();
        Assert.assertThat(result, containsString("PlaneModel"));
        Assert.assertThat(result, containsString("rowSize"));
        Assert.assertThat(result, containsString("rowHeight"));
        Assert.assertThat(result, containsString("groupList"));
    }
}