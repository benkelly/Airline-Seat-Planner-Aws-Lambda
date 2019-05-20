package ie.benjamin.airlineseatplanner.function;

import com.amazonaws.services.lambda.AWSLambda;
import ie.benjamin.airlineseatplanner.domain.ApiGatewayResponse;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import ie.benjamin.airlineseatplanner.service.FileService;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

class AirlineSeatPlannerLambdaFunctionTest {
    @Mock
    AWSLambda awsLambda;
    @Mock
    Logger log;
    @InjectMocks
    AirlineSeatPlannerLambdaFunction airlineSeatPlannerLambdaFunction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testApplyNull() {
        ApiGatewayResponse result = airlineSeatPlannerLambdaFunction.apply(null);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "☕️ & 🖤️");
        headers.put("Content-Type", "application/json");
        ApiGatewayResponse compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("Hello there! Add a JSON payload in request please! " +
                "\uD83D\uDC7B-  server time: "));
    }
    @Test
    void testApplyValidPlaneModel() throws FileNotFoundException {
        FileService fileService = new FileService();

        PlaneModel planeModel;
        planeModel = fileService.parsePlaneModel(new BufferedReader(
                new FileReader("src/test/resources/sample_data/base_case.txt")));

        ApiGatewayResponse result = airlineSeatPlannerLambdaFunction.apply(planeModel);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "☕️ & 🖤️");
        headers.put("Content-Type", "application/json");
        ApiGatewayResponse compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("rows"));
        Assert.assertThat(result.getBody(), containsString("rowSize"));
        Assert.assertThat(result.getBody(), containsString("assignedList"));
        Assert.assertThat(result.getBody(), containsString("passengerList"));
        Assert.assertThat(result.getBody(), containsString("maxSatisfaction"));
        Assert.assertThat(result.getBody(), containsString("percentageOfSatisfiedPassengers"));
        Assert.assertThat(result.getBody(), containsString("numberOfSatisfiedPassengers"));
        Assert.assertThat(result.getBody(), containsString("numberOfPassengersSeated"));
    }
    @Test
    void testApplyEmptyPlaneModel()  {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "☕️ & 🖤️");
        headers.put("Content-Type", "application/json");

        PlaneModel planeModel = PlaneModel.builder().rowHeight(0).rowSize(3).build();
        ApiGatewayResponse result = airlineSeatPlannerLambdaFunction.apply(planeModel);
        ApiGatewayResponse compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("Hello there! Add a JSON payload in request please! \uD83D\uDC7B-  server time:"));

        planeModel = PlaneModel.builder().rowHeight(2).rowSize(0).build();
        result = airlineSeatPlannerLambdaFunction.apply(planeModel);
        compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("Hello there! Add a JSON payload in request please! \uD83D\uDC7B-  server time:"));

        planeModel = PlaneModel.builder().rowHeight(2).rowSize(2).groupList(null).build();
        result = airlineSeatPlannerLambdaFunction.apply(planeModel);
        compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("Hello there! Add a JSON payload in request please! \uD83D\uDC7B-  server time:"));

        planeModel = PlaneModel.builder().rowHeight(2).rowSize(2).groupList(new ArrayList<>()).build();
        result = airlineSeatPlannerLambdaFunction.apply(planeModel);
        compApiGateway = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody(result.getBody())
                .build();

        Assertions.assertEquals(compApiGateway.getHeaders(), result.getHeaders());
        Assertions.assertEquals(compApiGateway.getStatusCode(), result.getStatusCode());
        Assert.assertThat(result.getBody(), containsString("Hello there! Add a JSON payload in request please! \uD83D\uDC7B-  server time:"));

    }
}