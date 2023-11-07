package br.com.fiap.parquimetro.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;


@Service @AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {

    private SesService sesService;

    public void sendMessage(SendEmailRequest sendEmailRequest) {
        this.sesService.sendMessage(sendEmailRequest);
    }
}
