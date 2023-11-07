package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.dto.FormaPagamentoDTO;
import br.com.fiap.parquimetro.model.FormaPagamento;
import br.com.fiap.parquimetro.service.FormaPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
    private final FormaPagamentoService formaPagamentoService;

    @PostMapping()
    public ResponseEntity<FormaPagamentoDTO> criarFormaPagamento(@RequestBody FormaPagamentoDTO formaPagamentoDTO) {
        return new ResponseEntity<>(formaPagamentoService.criarFormaPagamento(formaPagamentoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamento> obterFormaPagamento(@PathVariable String id) {
        Optional<FormaPagamento> formaPagamentoOptional = formaPagamentoService.obterFormaPagamento(id);

        if (formaPagamentoOptional.isPresent()) {
            FormaPagamento formaPagamento = formaPagamentoOptional.get();
            return new ResponseEntity<>(formaPagamento, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<FormaPagamentoDTO>> listarFormasPagamento() {
        return new ResponseEntity<>(formaPagamentoService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletarTodasFormasDePagamento() {
        formaPagamentoService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
