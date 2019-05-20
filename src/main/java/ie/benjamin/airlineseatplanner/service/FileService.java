package ie.benjamin.airlineseatplanner.service;


import ie.benjamin.airlineseatplanner.model.input.GroupModel;
import ie.benjamin.airlineseatplanner.model.input.PassengerModel;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Profile({"dev", "local"})
@Service
public class FileService {

    public PlaneModel parsePlaneModel(BufferedReader reader) {
        if (reader != null) {
            try {
                String planeSizeDef = reader.readLine();
                Matcher matcher = Pattern.compile("([0-9]*) ([0-9]*)")
                        .matcher(planeSizeDef);
                if (!matcher.find()) {
                    throw new IllegalArgumentException("Plane dimension matching error! regex pattern expected ([0-9]*) " +
                            "([0-9]*, was: " + planeSizeDef);
                }

                List<GroupModel> groups = reader.lines()
                        .map(FileService::parseGroupLine)
                        .collect(Collectors.toList());

                return PlaneModel.builder()
                        .groupList(groups)
                        .rowSize(Integer.valueOf(matcher.group(1)))
                        .rowHeight(Integer.valueOf(matcher.group(2)))
                        .build();
            } catch (IOException e) {
                throw new IllegalArgumentException("Parsing file exception - " + e);
            }
        } else {
            log.error("Parsing file exception - file null!");
            return null;
        }
    }

    private static GroupModel parseGroupLine(String line) {
        String[] passengersArray = line.split(" ");
        List<PassengerModel> passengers = Arrays.stream(passengersArray)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(FileService::parsePassenger)
                .collect(Collectors.toList());
        return GroupModel.builder()
                .passengerList(passengers)
                .build();
    }

    private static PassengerModel parsePassenger(String passengerData) {
        Matcher matcher = Pattern.compile("([0-9]*)(W)?", Pattern.CASE_INSENSITIVE)
                .matcher(passengerData);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("passenger matching error! regex pattern expected [0-9]*W?, was: "
                    + passengerData);
        }
        String name = matcher.group(1);
        Boolean windowSeatPreference = (matcher.group(2) != null);
        return PassengerModel.builder().build().builder()
                .name(name)
                .windowPref(windowSeatPreference)
                .build();
    }
}
