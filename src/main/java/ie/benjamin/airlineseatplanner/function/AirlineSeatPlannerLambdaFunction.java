package ie.benjamin.airlineseatplanner.function;

import com.amazonaws.services.lambda.AWSLambda;
import ie.benjamin.airlineseatplanner.domain.ApiGatewayResponse;
import ie.benjamin.airlineseatplanner.domain.PayloadResponse;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.planner.MonteCarloSeatPlanner;
import ie.benjamin.airlineseatplanner.planner.SeatPlanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component("AirlineSeatPlannerLambdaFunction")
public class AirlineSeatPlannerLambdaFunction implements Function<PlaneModel, ApiGatewayResponse> {

    @Autowired
    AWSLambda awsLambda;

    @Override
    public ApiGatewayResponse apply(PlaneModel payloadRequest) {
        Object responseBody;

        if (validatePlaneModel(payloadRequest)) {
            log.info("lambda Payload Request: {}", payloadRequest.toString());

            // planner logic
//            SeatPlanner seatPlanner = new SeatPlanner(payloadRequest);
//            responseBody = seatPlanner.resolve();

            MonteCarloSeatPlanner monteCarloSeatPlanner = new MonteCarloSeatPlanner(payloadRequest);
            responseBody = monteCarloSeatPlanner.resolve();

        } else {
            log.warn("lambda Payload Request empty  👻");
            responseBody =
                    new PayloadResponse("Hello there! Add a JSON payload in request please! 👻-  " +
                            "server time: " + new Date());
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "☕️ & 🖤️");
        headers.put("Content-Type", "application/json");
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(responseBody)
                .build();
    }

    private boolean validatePlaneModel(PlaneModel planeModel) {
        if (planeModel != null) {
            if (planeModel.getRowSize() > 0 & planeModel.getRowHeight() > 0) {
                if (planeModel.getGroupList() != null) {
                    return !planeModel.getGroupList().isEmpty();
                }
            }
        }
        return false;
    }
}
