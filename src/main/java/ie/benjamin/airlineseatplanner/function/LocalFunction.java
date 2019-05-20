package ie.benjamin.airlineseatplanner.function;

import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.planner.MonteCarloSeatPlanner;
import ie.benjamin.airlineseatplanner.service.FileService;
import ie.benjamin.airlineseatplanner.planner.SeatPlanner;
import ie.benjamin.airlineseatplanner.utilities.ShutdownManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Profile({"dev","local"})
@Slf4j
@Component
public class LocalFunction implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    ShutdownManager shutdownManager;

    @Autowired
    FileService fileService;

    @Value("${f:}")
    private String fileName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (fileName != null) {
            PlaneModel planeModel;
            try {
                 planeModel = fileService
                        .parsePlaneModel(new BufferedReader(new FileReader(fileName)));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("FileReader exception - " + e);
            }

            log.info("Naive Implementation");
            SeatPlanner seatPlanner = new SeatPlanner(planeModel);
            seatPlanner.resolve().print();

            log.info("Monte Carlo Implementation");
            MonteCarloSeatPlanner monteCarloSeatPlanner = new MonteCarloSeatPlanner(planeModel);
            monteCarloSeatPlanner.resolve().print();

            shutdownManager.initiateShutdown(0);
        }
        shutdownManager.initiateShutdown(128);
    }
}
