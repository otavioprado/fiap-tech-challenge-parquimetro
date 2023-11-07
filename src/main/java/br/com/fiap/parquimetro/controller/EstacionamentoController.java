package br.com.fiap.parquimetro.controller;

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
import br.com.fiap.parquimetro.service.EstacionamentoService;
import br.com.fiap.parquimetro.service.SQSService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estacionamentos")
@AllArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoRepository estacionamentoRepository;
    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstacionamentoService estacionamentoService;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final EstacionamentoMapper estacionamentoMapper;
    private final SQSService sqsService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Estacionamento> criarEstacionamento(@RequestBody IniciarEstacionamentoDTO iniciarEstacionamentoDTO) {
        Estacionamento novoEstacionamento = estacionamentoService.criarEstacionamento(iniciarEstacionamentoDTO);
        return new ResponseEntity<>(novoEstacionamento, HttpStatus.CREATED);
    }

    public void calcularValor(Estacionamento estacionamento) {
        if (estacionamento.getEntrada() != null && estacionamento.getSaida() != null) {
            long minutosEstacionado = Duration.between(estacionamento.getEntrada(), estacionamento.getSaida()).toMinutes();
            double horasEstacionado = minutosEstacionado / 60.0;

            int horasCheias = (int) Math.ceil(horasEstacionado);

            estacionamento.setValor(horasCheias * 10);
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

        if (estacionamento.getTipo() != TipoPeriodoEstacionamento.POR_HORA) {
            throw new DataIntegrityViolationException("Somente estacionamentos do tipo POR_HORA podem ter o horário de saída atualizado.");
        }

        if (estacionamento.getCondutor().getFormaPagamentos()
                .stream()
                .map(FormaPagamento::getId)
                .noneMatch(formaPagamentoId -> formaPagamentoId.equals(atualizarSaidaDTO.getFormaPagamentoId()))) {
            throw new DataIntegrityViolationException("Forma de pagamento não encontrada para o condutor.");
        }        //TODO : revisar

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

        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}


