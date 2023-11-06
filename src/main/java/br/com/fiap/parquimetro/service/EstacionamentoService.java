package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.repository.EstacionamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstacionamentoService {
    final EstacionamentoRepository estacionamentoRepository;


    public EstacionamentoService(EstacionamentoRepository estacionamentoRepository) {
        this.estacionamentoRepository = estacionamentoRepository;
    }

    public List<Estacionamento> encontrarEstacionamentosComTempoExpirando() {
        LocalDateTime tempoMinutosAntes = LocalDateTime.now().minusMinutes(5L);
       return estacionamentoRepository.findAllBySaidaIsNullAndEntradaLessThanEqual(tempoMinutosAntes);
    }
}
