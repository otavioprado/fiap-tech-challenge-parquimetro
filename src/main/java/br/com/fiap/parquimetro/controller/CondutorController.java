package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.service.CondutorService;
import br.com.fiap.parquimetro.service.VincularService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/condutores")
@AllArgsConstructor
public class CondutorController {

    private final CondutorService condutorService;
    private final VincularService vincularService;

    @PostMapping()
    public ResponseEntity<Condutor> registrarCondutor(@RequestBody Condutor condutor) {
        Condutor novoCondutor = condutorService.registrarCondutor(condutor);
        return new ResponseEntity<>(novoCondutor, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Condutor>> listarTodosOsCondutores() {
        List<Condutor> condutores = condutorService.listarTodosOsCondutores();
        return new ResponseEntity<>(condutores, HttpStatus.OK);
    }

    @GetMapping("/{condutorId}")
    public ResponseEntity<Condutor> obterCondutor(@PathVariable String condutorId) {
        Optional<Condutor> condutor = condutorService.obterCondutorPorId(condutorId);
        return condutor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{condutorId}/veiculos")
    public ResponseEntity<String> vincularVeiculoAoCondutor(@PathVariable String condutorId,
                                                            @RequestParam String veiculoId) {
        return vincularService.vincularVeiculoAoCondutor(condutorId, veiculoId);
    }

    @GetMapping("/{condutorId}/veiculos")
    public ResponseEntity<List<Veiculo>> obterVeiculosDoCondutor(@PathVariable String condutorId) {
        return condutorService.obterVeiculosDoCondutor(condutorId);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletarTodosOsCondutores() {
        condutorService.deletarTodosOsCondutores();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
