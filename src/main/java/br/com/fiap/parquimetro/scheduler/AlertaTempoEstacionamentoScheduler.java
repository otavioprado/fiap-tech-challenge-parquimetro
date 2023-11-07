package br.com.fiap.parquimetro.scheduler;

import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.service.EmailService;
import br.com.fiap.parquimetro.service.EstacionamentoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Component
public class AlertaTempoEstacionamentoScheduler {

    final EstacionamentoService estacionamentoService;
    final EmailService emailService;

    @Value("${parquimetro.email-noreply}")
    String fromEmail;

    private static final String TIME_ZONE = "America/Sao_Paulo";
    private static final String CADA_5MINUTOS = "0 0/5 * 1/1 * ?"; //http://www.cronmaker.com/

    public AlertaTempoEstacionamentoScheduler(EstacionamentoService estacionamentoService, EmailService emailService) {
        this.estacionamentoService = estacionamentoService;
        this.emailService = emailService;
    }

    @Scheduled(cron = CADA_5MINUTOS, zone = TIME_ZONE)
    public void verificarExpiracaoTempo() {
        List<Estacionamento> estacionamentosComTempoExpirando =
                estacionamentoService.encontrarEstacionamentosComTempoExpirando();

        SendEmailRequest.Builder builder = SendEmailRequest.builder();

        builder.source(fromEmail); // from

        estacionamentosComTempoExpirando.forEach(estacionamento -> {
            System.out.println("Enviando alerta para o condutor " + estacionamento.getCondutor().getNome());

            builder.destination(Destination.builder().toAddresses(estacionamento.getCondutor().getEmail()).build());

            if (TipoPeriodoEstacionamento.PERIODO_FIXO.equals(estacionamento.getTipo())) {
                // Para períodos fixos, o sistema emite um alerta informando que o tempo de estacionamento está
                // prestes a expirar e que o condutor deve desligar o registro.
                builder.message(
                        Message.builder()
                                .subject(Content.builder().data("Alerta de expiração de tempo de estacionamento").build())
                                .body(Body.builder().text(Content.builder().data("O tempo de estacionamento está prestes a expirar. Desligue o registro.").build()).build()
                                ).build());
            }

            if (TipoPeriodoEstacionamento.POR_HORA.equals(estacionamento.getTipo())) {
                // Para períodos variáveis, o sistema também emite um alerta informando que o sistema estenderá
                // automaticamente o estacionamento por mais uma hora, a menos que o condutor desligue o registro.
                builder.message(
                        Message.builder()
                                .subject(Content.builder().data("Alerta de expiração de tempo de estacionamento").build())
                                .body(Body.builder().text(Content.builder().data("O tempo de estacionamento está prestes a expirar. O sistema estenderá automaticamente o estacionamento por mais uma hora, a menos que o condutor desligue o registro.").build()).build()
                                ).build());
            }

            emailService.sendMessage(builder.build());
        });
    }
}
