package ie.benjamin.airlineseatplanner.function;

import com.ginsberg.junit.exit.ExpectSystemExit;
import ie.benjamin.airlineseatplanner.service.FileService;
import ie.benjamin.airlineseatplanner.utilities.ShutdownManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class LocalFunctionTest {
    @Mock
    ShutdownManager shutdownManager;
    @Mock
    FileService fileService;
    @Mock
    Logger log;
    @InjectMocks
    LocalFunction localFunction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
//    @ExpectSystemExit
    void testOnApplicationEvent() {
        when(fileService.parsePlaneModel(any())).thenReturn(null);
        localFunction.onApplicationEvent(null);
    }
}