package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.dto.AtualizarSaidaEstacionamentoDTO;
import br.com.fiap.parquimetro.dto.IniciarEstacionamentoDTO;
import br.com.fiap.parquimetro.model.Estacionamento;
import br.com.fiap.parquimetro.repository.EstacionamentoRepository;
import br.com.fiap.parquimetro.service.EstacionamentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estacionamentos")
@AllArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoRepository estacionamentoRepository;
    private final EstacionamentoService estacionamentoService;

    @PostMapping
    public ResponseEntity<Estacionamento> criarEstacionamento(@RequestBody IniciarEstacionamentoDTO iniciarEstacionamentoDTO) {
        Estacionamento novoEstacionamento = estacionamentoService.criarEstacionamento(iniciarEstacionamentoDTO);
        return new ResponseEntity<>(novoEstacionamento, HttpStatus.CREATED);
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
    public ResponseEntity<Estacionamento> atualizarSaidaEstacionamento(
            @PathVariable String id,
            @RequestBody AtualizarSaidaEstacionamentoDTO atualizarSaidaDTO
    ) {
        return new ResponseEntity<>(
                estacionamentoService.atualizarSaidaEstacionamento(id, atualizarSaidaDTO), HttpStatus.OK);
    }
}


