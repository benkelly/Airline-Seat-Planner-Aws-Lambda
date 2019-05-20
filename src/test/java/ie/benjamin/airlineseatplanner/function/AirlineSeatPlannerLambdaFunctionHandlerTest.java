package ie.benjamin.airlineseatplanner.function;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.benjamin.airlineseatplanner.config.AwsClientConfig;
import ie.benjamin.airlineseatplanner.domain.ApiGatewayResponse;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import sun.net.www.MessageHeader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;

class AirlineSeatPlannerLambdaFunctionHandlerTest {
    @Mock
    ObjectMapper mapper;
    @Mock
    FunctionInspector inspector;
    @Mock
    Logger log;
    @InjectMocks
    AirlineSeatPlannerLambdaFunctionHandler airlineSeatPlannerLambdaFunctionHandler;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
    }

    private SpringBootRequestHandler<PlaneModel, ApiGatewayResponse> handler;

    @Test
    public void testFunctionBean() throws Exception {
        PlaneModel planeModel = PlaneModel.builder().rowHeight(0).rowSize(0).build();
        this.handler = new SpringBootRequestHandler<PlaneModel, ApiGatewayResponse>(FunctionConfig.class);
        Object output = this.handler.handleRequest(planeModel, null);
        assertThat(output).isInstanceOf(ApiGatewayResponse.class);
    }

    @Test
    public void testFunctionBeanApiHandlerNull() throws Exception {
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody(null);
        Object output = airlineSeatPlannerLambdaFunctionHandler.convertEvent(event);
        assertThat(output).isInstanceOf(PlaneModel.class);
    }

    @Test
    public void testFunctionBeanApiHandlerEmptyJson() throws Exception {
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody("{}");
        Object output = airlineSeatPlannerLambdaFunctionHandler.convertEvent(event);
        Assert.assertNull(output);
    }

    @Test
    public void testFunctionBeanApiHandlerValidJson() throws Exception {
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setHeaders(headers);
        event.setResource("airlineSeatPlannerLambdaFunction");
        event.setBody("{\"rowSize\":4,\"rowHeight\":4,\"groupList\":[{\"passengerList\":" +
                "[{\"windowPref\":true,\"name\":\"1\"},{\"windowPref\":false,\"name\":\"2\"}," +
                "{\"windowPref\":false,\"name\":\"3\"}]},{\"passengerList\":[{\"windowPref\":" +
                "false,\"name\":\"4\"},{\"windowPref\":false,\"name\":\"5\"},{\"windowPref\":" +
                "false,\"name\":\"6\"},{\"windowPref\":false,\"name\":\"7\"}]},{\"passengerList\":" +
                "[{\"windowPref\":false,\"name\":\"8\"}]},{\"passengerList\":[{\"windowPref\":false," +
                "\"name\":\"9\"},{\"windowPref\":false,\"name\":\"10\"},{\"windowPref\":true,\"name\"" +
                ":\"11\"}]},{\"passengerList\":[{\"windowPref\":true,\"name\":\"12\"}]},{\"passengerList" +
                "\":[{\"windowPref\":false,\"name\":\"13\"},{\"windowPref\":false,\"name\":\"14\"}]}," +
                "{\"passengerList\":[{\"windowPref\":false,\"name\":\"15\"},{\"windowPref\":false," +
                "\"name\":\"16\"}]}]}");
        Object output = airlineSeatPlannerLambdaFunctionHandler.convertEvent(event);
        Assert.assertNull(output);
    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionConfig {

        @Bean
        public AWSLambda awsLambdaClientEnvDefault() {
            return AWSLambdaAsyncClientBuilder.standard().withRegion("us-east-1")
                    .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
        }

        @Bean
        public AirlineSeatPlannerLambdaFunction airlineSeatPlannerLambdaFunction() {
            return new AirlineSeatPlannerLambdaFunction();
        }
    }

    @Test
    public void testPrivateGetHeaders() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method method = AirlineSeatPlannerLambdaFunctionHandler.class.getDeclaredMethod("getHeaders", APIGatewayProxyRequestEvent.class);
        method.setAccessible(true);
        APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
        apiGatewayProxyRequestEvent.setResource("resource");
        apiGatewayProxyRequestEvent.setHeaders(headers);
        apiGatewayProxyRequestEvent.setBody("body");
        Object output = method.invoke(airlineSeatPlannerLambdaFunctionHandler, apiGatewayProxyRequestEvent);
        Assert.assertThat(output, notNullValue());
    }
}