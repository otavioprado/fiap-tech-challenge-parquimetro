package br.com.fiap.parquimetro.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class SesService {

    private final SesClient sesClient;

    public SesService(@Value("${aws.region}") String region,
                      @Value("${aws.local.endpoint:#{null}}") String awsEndpoint) {
        SesClientBuilder builder = SesClient.builder();

        if (awsEndpoint != null) {
            // override aws endpoint with localstack URL in dev environment
            try {
                builder.endpointOverride(new URI(awsEndpoint));
            } catch (URISyntaxException ex) {
                log.error("Invalid url {}", awsEndpoint);
                throw new IllegalStateException("Invalid url " + awsEndpoint, ex);
            }
        }

        sesClient = builder.credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }

    public void sendMessage(SendEmailRequest sendEmailRequest) {
        sesClient.sendEmail(sendEmailRequest);
    }
}