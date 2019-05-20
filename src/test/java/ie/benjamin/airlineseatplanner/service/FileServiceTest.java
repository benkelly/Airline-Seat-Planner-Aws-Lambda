package ie.benjamin.airlineseatplanner.service;

import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class FileServiceTest {
    @Mock
    Logger log;
    @InjectMocks
    FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testParsePlaneModelNull() {
        PlaneModel result = fileService.parsePlaneModel(null);
        Assertions.assertEquals(null, result);
    }
    @Test
    void testParsePlaneModel() throws FileNotFoundException {
        PlaneModel result = fileService.parsePlaneModel(
                new BufferedReader(
                new FileReader("src/test/resources" +
                "/sample_data/base_case.txt")));
        Assert.assertThat(result.toString(), containsString("PlaneModel"));
        Assert.assertThat(result.toString(), containsString("rowSize"));
        Assert.assertThat(result.toString(), containsString("rowHeight"));
        Assert.assertThat(result.toString(), containsString("groupList"));
        Assert.assertThat(result.toString(), containsString("GroupModel"));
        Assert.assertThat(result.toString(), containsString("passengerList"));
        Assert.assertThat(result.toString(), containsString("PassengerModel"));
        Assert.assertThat(result.toString(), containsString("windowPref"));
        Assert.assertThat(result.toString(), containsString("name"));
    }
}