package ie.benjamin.airlineseatplanner.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsClientConfig {

    @Bean
    @Profile({"local","dev","api"})
    public AWSLambda awsLambdaClientEnvDefault(){
        return AWSLambdaAsyncClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    }

    @Bean
    @Profile("localstack")
    public AWSLambda awsLambdaClientLocalStack(){
        return AWSLambdaAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration("http://localstack:4572","us-west-1"))
                .build();
    }

    @Bean
    @Profile("aws")
    public AWSLambda awsLambdaClientAws() {
        return AWSLambdaAsyncClientBuilder.defaultClient();
    }

}
