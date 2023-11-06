package br.com.fiap.parquimetro.scheduler;

import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.service.EstacionamentoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertaTempoEstacionamentoScheduler {

    final EstacionamentoService estacionamentoService;

    private static final String TIME_ZONE = "America/Sao_Paulo";
    private static final String CADA_5MINUTOS = "0 0/5 * 1/1 * ?"; //http://www.cronmaker.com/

    public AlertaTempoEstacionamentoScheduler(EstacionamentoService estacionamentoService) {
        this.estacionamentoService = estacionamentoService;
    }

    @Scheduled(cron = CADA_5MINUTOS, zone = TIME_ZONE)
    public void verificarExpiracaoTempo() {
        List<Estacionamento> estacionamentosComTempoExpirando = estacionamentoService.encontrarEstacionamentosComTempoExpirando();
        estacionamentosComTempoExpirando.forEach(estacionamento -> {
            System.out.println("Enviando alerta para o condutor " + estacionamento.getCondutor().getNome());

            if (TipoPeriodoEstacionamento.PERIODO_FIXO.equals(estacionamento.getTipo())) {
                // Para períodos fixos, o sistema emite um alerta informando que o tempo de estacionamento está
                // prestes a expirar e que o condutor deve desligar o registro.
            }

            if (TipoPeriodoEstacionamento.POR_HORA.equals(estacionamento.getTipo())) {
                // Para períodos variáveis, o sistema também emite um alerta informando que o sistema estenderá
                // automaticamente o estacionamento por mais uma hora, a menos que o condutor desligue o registro.
            }
        });
    }
}
