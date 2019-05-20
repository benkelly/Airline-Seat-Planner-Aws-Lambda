package ie.benjamin.airlineseatplanner.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;


class FileArgCommandLineRunnerTest {
    @Mock
    ShutdownManager shutdownManager;
    @Mock
    Logger log;
    @InjectMocks
    FileArgCommandLineRunner fileArgCommandLineRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRunNullFile() throws Exception {
        fileArgCommandLineRunner.run("args");
        fileArgCommandLineRunner.run(null);
    }

}