package br.com.fiap.parquimetro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class SQSService {

    private final SqsClient sqsClient;
    private final String queueUrl;

    public SQSService(@Value("${aws.sqs.queueUrl}") String queueUrl,
                      @Value("${aws.region}") String region,
                      @Value("${aws.local.endpoint:#{null}}") String awsEndpoint) {
        this.queueUrl = queueUrl;
        SqsClientBuilder builder = SqsClient.builder();

        if (awsEndpoint != null) {
            // override aws endpoint with localstack URL in dev environment
            try {
                builder.endpointOverride(new URI(awsEndpoint));
            } catch (URISyntaxException ex) {
                log.error("Invalid url {}", awsEndpoint);
                throw new IllegalStateException("Invalid url " + awsEndpoint, ex);
            }
        }

        sqsClient = builder.credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }

    public void sendMessage(String message) {
        log.debug("Sending message: {}", message);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(request);
    }
}