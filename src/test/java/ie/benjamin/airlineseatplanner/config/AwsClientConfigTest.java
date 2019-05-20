package ie.benjamin.airlineseatplanner.config;

import com.amazonaws.services.lambda.AWSLambda;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AwsClientConfigTest {
    AwsClientConfig awsClientConfig = new AwsClientConfig();

    @Test
    void testAwsLambdaClientEnvDefault() {
        AWSLambda result = awsClientConfig.awsLambdaClientEnvDefault();
        Assert.assertNotNull(result);
    }

    @Test
    void testAwsLambdaClientLocalStack() {
        AWSLambda result = awsClientConfig.awsLambdaClientLocalStack();
        Assert.assertNotNull(result);
    }
}