package br.com.fiap.parquimetro.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class alertaTempoEstacionamento {

    private static final String TIME_ZONE = "America/Sao_Paulo";
    private static final String CADA_5MINUTOS = "0 0/5 * 1/1 * ? *";
    @Scheduled(cron = CADA_5MINUTOS, zone = TIME_ZONE)
    public void verificarExpiracaoTempoFixo() {
        //- O sistema inclui um recurso de alerta que notifica o condutor quando o tempo de estacionamento está
        // prestes a expirar, no caso de horário fixo.
    }
    @Scheduled(cron = CADA_5MINUTOS, zone = TIME_ZONE)
    public void verificarExpiracaoTempoVariavel(){
       // Para períodos variáveis, o sistema também emite um alerta informando que o sistema estenderá
       // automaticamente o estacionamento por mais uma hora, a menos que o condutor desligue o registro.
    }
}
