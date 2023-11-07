package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.dto.AtualizarSaidaEstacionamentoDTO;
import br.com.fiap.parquimetro.dto.IniciarEstacionamentoDTO;
import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import br.com.fiap.parquimetro.mapper.EstacionamentoMapper;
import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.model.FormaPagamento;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.EstacionamentoRepository;
import br.com.fiap.parquimetro.repository.FormaPagamentoRepository;
import br.com.fiap.parquimetro.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EstacionamentoService {
    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstacionamentoRepository estacionamentoRepository;
    private final EstacionamentoMapper estacionamentoMapper;
    private final SQSService sqsService;
    private final ObjectMapper objectMapper;
    private final FormaPagamentoRepository formaPagamentoRepository;

    public Estacionamento criarEstacionamento(IniciarEstacionamentoDTO iniciarEstacionamentoDTO) {

        String condutorId = iniciarEstacionamentoDTO.getCondutorId();
        Optional<Condutor> condutorOpt = condutorRepository.findById(condutorId);


        if (condutorOpt.isEmpty()) {
            throw new DataIntegrityViolationException("Condutor com ID não encontrado: " + condutorId);
        }

        String veiculoId = iniciarEstacionamentoDTO.getVeiculoId();
        Optional<Veiculo> veiculoOpt = veiculoRepository.findById(veiculoId);

        if (veiculoOpt.isEmpty()) {
            throw new DataIntegrityViolationException("Veículo com ID não encontrado: " + veiculoId);
        }

        if (condutorOpt.get().getVeiculos().stream().noneMatch(veiculo -> veiculo.getId().equals(veiculoId))) {
            String mensagemDeErro = "O veículo não está associado ao condutor. Veículo ID: " + veiculoId + ", Condutor ID: " + condutorId;
            throw new DataIntegrityViolationException(mensagemDeErro);
        }

        Estacionamento estacionamento = estacionamentoMapper.toEntity(iniciarEstacionamentoDTO);

        if (iniciarEstacionamentoDTO.getTipo() == TipoPeriodoEstacionamento.PERIODO_FIXO) {
            // informar saída é obrigatório
            if (iniciarEstacionamentoDTO.getSaida() == null) {
                throw new DataIntegrityViolationException("Informar a saída é obrigatório para tempo fixo.");
            }

            // calcular valor para estacionamento de tempo fixo
            //calcularValor(estacionamento);
        } else {
            // valor será calculado quando usuário atualizar via API PUT o horário de saída do estacionamento
            if (iniciarEstacionamentoDTO.getSaida() != null) {
                throw new DataIntegrityViolationException("Saída só pode ser informado quando tipo de estacionamento é PERIODO_FIXO");
            }
        }

        estacionamento.setCondutor(condutorOpt.get());
        estacionamento.setVeiculo(veiculoOpt.get());

        Estacionamento novoEstacionamento = estacionamentoRepository.save(estacionamento);

        try {
            String estacionamentoJSON = objectMapper.writeValueAsString(estacionamento);
            sqsService.sendMessage(estacionamentoJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return novoEstacionamento;
    }

    public List<Estacionamento> encontrarEstacionamentosComTempoExpirando() {
        LocalDateTime tempoMinutosAntes = LocalDateTime.now().minusMinutes(5L);
        return estacionamentoRepository.findAllBySaidaIsNullAndEntradaLessThanEqual(tempoMinutosAntes);
    }

    public Estacionamento atualizarSaidaEstacionamento(String id, AtualizarSaidaEstacionamentoDTO atualizarSaidaDTO) {
        Estacionamento estacionamento = estacionamentoRepository.findById(id)
            .orElseThrow(() -> new DataIntegrityViolationException("Estacionamento não encontrado com o ID: "
                    + id));

        if (estacionamento.getTipo() != TipoPeriodoEstacionamento.POR_HORA) {
            throw new DataIntegrityViolationException("Somente estacionamentos do tipo POR_HORA podem ter o " +
                "horário de saída atualizado.");
        }

        if (estacionamento.getCondutor().getFormaPagamentos()
            .stream()
            .map(FormaPagamento::getId)
            .noneMatch(formaPagamentoId -> formaPagamentoId.equals(atualizarSaidaDTO.getFormaPagamentoId()))
        ) {
            throw new DataIntegrityViolationException("Forma de pagamento não encontrada para o condutor.");
        }

        estacionamento.setSaida(atualizarSaidaDTO.getSaida());
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(atualizarSaidaDTO.getFormaPagamentoId())
            .orElseThrow(() -> new DataIntegrityViolationException("Forma de pagamento não encontrada com o ID: "
                    + atualizarSaidaDTO.getFormaPagamentoId()));

        estacionamento.setPagamento(formaPagamento);
        calcularValor(estacionamento);

        try {
            String estacionamentoJSON = objectMapper.writeValueAsString(estacionamento);
            sqsService.sendMessage(estacionamentoJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Estacionamento save = estacionamentoRepository.save(estacionamento);

        return save;
    }


    public void calcularValor(Estacionamento estacionamento) {
        if (estacionamento.getEntrada() != null && estacionamento.getSaida() != null) {
            long minutosEstacionado = Duration.between(estacionamento.getEntrada(), estacionamento.getSaida()).toMinutes();
            double horasEstacionado = minutosEstacionado / 60.0;

            int horasCheias = (int) Math.ceil(horasEstacionado);

            estacionamento.setValor(horasCheias * 10);
        }
    }
}
