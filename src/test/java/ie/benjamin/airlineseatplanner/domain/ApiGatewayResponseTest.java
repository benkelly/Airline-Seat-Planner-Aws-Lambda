package ie.benjamin.airlineseatplanner.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Mockito.*;

class ApiGatewayResponseTest {
    ApiGatewayResponse apiGatewayResponse;

    @BeforeEach
    void setUp() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        apiGatewayResponse = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(headers)
                .setObjectBody("responseBody")
                .build();
    }

    @Test
    void testBuilder() {
        ApiGatewayResponse.Builder result = ApiGatewayResponse.builder();
        Assertions.assertNotEquals(new ApiGatewayResponse.Builder(), result);
    }

    @Test
    void testEquals() {
        ApiGatewayResponse compResponse = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(apiGatewayResponse.getHeaders())
                .setObjectBody("responseBody")
                .build();
        Assertions.assertEquals(true, apiGatewayResponse.equals(compResponse));

        ApiGatewayResponse compResponse1 = ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(apiGatewayResponse.getHeaders())
                .setObjectBody("responseBody")
                .build();
        Assertions.assertEquals(false, apiGatewayResponse.equals(compResponse1));
    }

    @Test
    void testCanEqual() {
        ApiGatewayResponse compResponse = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(apiGatewayResponse.getHeaders())
                .setObjectBody("responseBody")
                .build();
        Assertions.assertEquals(true, apiGatewayResponse.canEqual(compResponse));

        ApiGatewayResponse compResponse1 = ApiGatewayResponse.builder()
                .setStatusCode(2002)
                .setHeaders(apiGatewayResponse.getHeaders())
                .setObjectBody("responseBody")
                .build();
        Assertions.assertEquals(true, apiGatewayResponse.canEqual(compResponse1));
    }

    @Test
    void testHashCode() {
        ApiGatewayResponse compResponse = ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(apiGatewayResponse.getHeaders())
                .setObjectBody("responseBody")
                .build();
        Assertions.assertEquals(compResponse.hashCode(), apiGatewayResponse.hashCode());
    }

    @Test
    void testToString() {
        String result = apiGatewayResponse.toString();

        Assert.assertThat(result, containsString("statusCode"));
        Assert.assertThat(result, containsString("headers"));
        Assert.assertThat(result, containsString("isBase64Encoded"));
    }
}
