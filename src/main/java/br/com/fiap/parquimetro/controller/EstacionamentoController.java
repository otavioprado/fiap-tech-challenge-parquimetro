package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.dto.AtualizarSaidaEstacionamentoDTO;
import br.com.fiap.parquimetro.dto.IniciarEstacionamentoDTO;
import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import br.com.fiap.parquimetro.mapper.EstacionamentoMapper;
import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.EstacionamentoRepository;
import br.com.fiap.parquimetro.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estacionamentos")
@AllArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoRepository estacionamentoRepository;
    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstacionamentoMapper estacionamentoMapper;

    @PostMapping
    public ResponseEntity<Estacionamento> criarEstacionamento(@RequestBody IniciarEstacionamentoDTO iniciarEstacionamentoDTO) {
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
        if(iniciarEstacionamentoDTO.getTipo() == TipoPeriodoEstacionamento.PERIODO_FIXO) {
            // informar saída é obrigatório
            if(iniciarEstacionamentoDTO.getSaida() == null) {
                throw new DataIntegrityViolationException("Informar a saída é obrigatório para tempo fixo.");
            }

            // calcular valor para estacionamento de tempo fixo
            calcularValor(estacionamento);
        } else {
            // valor será calculado quando usuário atualizar via API PUT o horário de saída do estacionamento
            if(iniciarEstacionamentoDTO.getSaida() != null) {
                throw new DataIntegrityViolationException("Saída só pode ser informado quando tipo de estacionamento é PERIODO_FIXO");
            }
        }

        estacionamento.setCondutor(condutorOpt.get());
        estacionamento.setVeiculo(veiculoOpt.get());

        Estacionamento novoEstacionamento = estacionamentoRepository.save(estacionamento);
        return new ResponseEntity<>(novoEstacionamento, HttpStatus.CREATED);
    }

    public void calcularValor(Estacionamento estacionamento) {
        if (estacionamento.getEntrada() != null && estacionamento.getSaida() != null) {
            long minutosEstacionado = Duration.between(estacionamento.getEntrada(), estacionamento.getSaida()).toMinutes();
            double horasEstacionado = minutosEstacionado / 60.0;
            estacionamento.setValor(horasEstacionado * 10);
        } else {
            estacionamento.setValor(0);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estacionamento> obterEstacionamento(@PathVariable String id) {
        Optional<Estacionamento> estacionamentoOptional = estacionamentoRepository.findById(id);
        return estacionamentoOptional.map(estacionamento -> new ResponseEntity<>(estacionamento, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Estacionamento>> listarEstacionamentos() {
        List<Estacionamento> estacionamentos = estacionamentoRepository.findAll();
        return new ResponseEntity<>(estacionamentos, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> excluirEstacionamentos() {
        estacionamentoRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estacionamento> atualizarSaidaEstacionamento(@PathVariable String id, @RequestBody AtualizarSaidaEstacionamentoDTO atualizarSaidaDTO) {
        Estacionamento estacionamento = estacionamentoRepository.findById(id)
                .orElseThrow(() -> new DataIntegrityViolationException("Estacionamento não encontrado com o ID: " + id));

        if(estacionamento.getTipo() != TipoPeriodoEstacionamento.POR_HORA) {
            throw new DataIntegrityViolationException("Somente estacionamentos do tipo POR_HORA podem ter o horário de saída atualizado.");
        }

        LocalDateTime novaSaida = atualizarSaidaDTO.getSaida();
        estacionamento.setSaida(novaSaida);

        calcularValor(estacionamento);
        Estacionamento save = estacionamentoRepository.save(estacionamento);

        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}


