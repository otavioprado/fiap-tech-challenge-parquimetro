package br.com.fiap.parquimetro.service;

import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;


@Service @AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {
    private SimpleEmailServiceMailSender mailSender;
    public void sendMessage(SimpleMailMessage simpleMailMessage) {
        this.mailSender.send(simpleMailMessage);
    }
}
