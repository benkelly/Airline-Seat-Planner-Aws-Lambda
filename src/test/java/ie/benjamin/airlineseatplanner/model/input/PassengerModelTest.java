package ie.benjamin.airlineseatplanner.model.input;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class PassengerModelTest {

    PassengerModel passengerModel;

    @BeforeEach
    void setUp() {
        passengerModel = PassengerModel.builder().build();
    }

    @Test
    void testBuilder() {
        PassengerModel.PassengerModelBuilder result = PassengerModel.builder();
        Assertions.assertNotEquals(new PassengerModel.PassengerModelBuilder(), result);

    }

    @Test
    void testSetWindowPref() {
        passengerModel.setWindowPref(true);
    }

    @Test
    void testSetName() {
        passengerModel.setName("name");
    }

    @Test
    void testEquals() {
        boolean result = passengerModel.equals(new PassengerModel.PassengerModelBuilder().build());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCanEqual() {
        boolean result = passengerModel.canEqual(new PassengerModel.PassengerModelBuilder().build());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHashCode() {
        int result = passengerModel.hashCode();
        Assertions.assertEquals(new PassengerModel.PassengerModelBuilder().build().hashCode(), result);
    }

    @Test
    void testToString() {
        String result = passengerModel.toString();
        Assert.assertThat(result, containsString("PassengerModel"));
        Assert.assertThat(result, containsString("windowPref"));
        Assert.assertThat(result, containsString("name"));
    }
}