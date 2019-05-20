package ie.benjamin.airlineseatplanner.model.input;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

class GroupModelBuilderTest {
    @Mock
    List<PassengerModel> passengerList;
    @InjectMocks
    GroupModel.GroupModelBuilder groupModelBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPassengerList() {
        GroupModel.GroupModelBuilder result = groupModelBuilder.passengerList(Arrays.asList(new PassengerModel(true, "name")));
        Assert.assertThat(result.toString(), containsString("GroupModel.GroupModelBuilder(passengerList=[PassengerModel"));

    }

    @Test
    void testBuild() {
        GroupModel result = groupModelBuilder.build();
        Assertions.assertNotEquals(new GroupModel.GroupModelBuilder(), result);
    }

    @Test
    void testToString() {
        String result = groupModelBuilder.toString();
        Assert.assertThat(result, containsString("passengerList"));
    }
}