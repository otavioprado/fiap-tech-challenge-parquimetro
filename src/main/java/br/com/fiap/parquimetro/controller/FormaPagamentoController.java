package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.dto.FormaPagamentoDTO;
import br.com.fiap.parquimetro.mapper.FormaPagamentoMapper;
import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.FormaPagamento;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.FormaPagamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final CondutorRepository condutorRepository;
    private final FormaPagamentoMapper formaPagamentoMapper;

    @PostMapping()
    public ResponseEntity<FormaPagamentoDTO> criarFormaPagamento(@RequestBody FormaPagamentoDTO formaPagamentoDTO) {
        String condutorId = formaPagamentoDTO.getCondutorId();
        Optional<Condutor> condutorOpt = condutorRepository.findById(condutorId);
        if (condutorOpt.isEmpty()) {
            throw new DataIntegrityViolationException("Condutor com ID não encontrado: " + condutorId);
        }

        FormaPagamento novaFormaPagamento = formaPagamentoRepository.save(formaPagamentoMapper.toEntity(formaPagamentoDTO));
        Condutor condutor = condutorOpt.get();

        if (condutor.getFormaPagamentos() == null) {
            condutor.setFormaPagamentos(List.of(novaFormaPagamento));
        } else {
            condutor.getFormaPagamentos().add(novaFormaPagamento);
        }

        condutorRepository.save(condutor);

        return new ResponseEntity<>(formaPagamentoMapper.toDTO(novaFormaPagamento), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamento> obterFormaPagamento(@PathVariable String id) {
        Optional<FormaPagamento> formaPagamentoOptional = formaPagamentoRepository.findById(id);

        if (formaPagamentoOptional.isPresent()) {
            FormaPagamento formaPagamento = formaPagamentoOptional.get();
            return new ResponseEntity<>(formaPagamento, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<FormaPagamento>> listarFormasPagamento() {
        List<FormaPagamento> formasPagamento = formaPagamentoRepository.findAll();
        return new ResponseEntity<>(formasPagamento, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletarTodasFormasDePagamento() {
        formaPagamentoRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
