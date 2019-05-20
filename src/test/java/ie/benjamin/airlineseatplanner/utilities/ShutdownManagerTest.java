package ie.benjamin.airlineseatplanner.utilities;

import com.ginsberg.junit.exit.ExpectSystemExit;
import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

class ShutdownManagerTest {

    @Mock
    ApplicationContext appContext;
    @InjectMocks
    ShutdownManager shutdownManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @ExpectSystemExit
    void testInitiateShutdown() {

        shutdownManager.initiateShutdown(0);
    }

    @Test
    //    @ExpectSystemExit
    @ExpectSystemExitWithStatus(128)
    void testInitiateShutdownInvalidArg() {


        shutdownManager.initiateShutdown(128);

    }
}