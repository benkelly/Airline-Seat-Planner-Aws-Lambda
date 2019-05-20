package ie.benjamin.airlineseatplanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class AirlineSeatPlannerApplicationTest {
    @Mock
    Logger log;
    @InjectMocks
    AirlineSeatPlannerApplication airlineSeatPlannerApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMain() {

//        AirlineSeatPlannerApplication.main(new String[]{"args"});

        AirlineSeatPlannerApplication.main(new String[]{
                "--spring.main.web-environment=false",
//                "--spring.autoconfigure.exclude=AWSLambda",
        });
    }
}