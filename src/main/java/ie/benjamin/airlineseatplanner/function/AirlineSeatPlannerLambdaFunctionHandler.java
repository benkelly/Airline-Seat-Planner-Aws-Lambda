package ie.benjamin.airlineseatplanner.function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.benjamin.airlineseatplanner.domain.ApiGatewayResponse;
import ie.benjamin.airlineseatplanner.model.input.PlaneModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AirlineSeatPlannerLambdaFunctionHandler extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse>{
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private FunctionInspector inspector;

    /**
     * @param event Handler for Request from AWS API Gateway in to ƛ
     * @return
     */
    @Override
    protected Object convertEvent(APIGatewayProxyRequestEvent event) {
        PlaneModel planeModel = null;
        try {
            if (event.getBody() != null) {
                planeModel = mapper.readValue(event.getBody(), PlaneModel.class);
            } else {
                // if null json was sent.
                planeModel = PlaneModel.builder().groupList(null)
                        .rowHeight(0)
                        .rowSize(0)
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (functionAcceptsMessage()) {
            return new GenericMessage<>(planeModel, getHeaders(event));
        }
        else {
            return planeModel;
        }
    }

    private boolean functionAcceptsMessage() {
        return this.inspector.isMessage(function());
    }

    private MessageHeaders getHeaders(APIGatewayProxyRequestEvent event) {
        Map<String, Object> headers = new HashMap<>();
        if (event.getHeaders() != null) {
            headers.putAll(event.getHeaders());
        }
        headers.put("request", event);
        return new MessageHeaders(headers);
    }
}
