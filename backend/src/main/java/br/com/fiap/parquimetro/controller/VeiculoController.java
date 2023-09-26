package br.com.fiap.parquimetro.controller;

import br.com.fiap.parquimetro.dto.VeiculoDTO;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping()
    public ResponseEntity<Veiculo> registrarVeiculo(@RequestBody VeiculoDTO veiculoDTO) {
        Veiculo novoVeiculo = veiculoService.registrarVeiculo(veiculoDTO);
        return new ResponseEntity<>(novoVeiculo, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<VeiculoDTO>> listarTodosOsVeiculos() {
        List<VeiculoDTO> veiculos = veiculoService.listarTodosOsVeiculos();
        return new ResponseEntity<>(veiculos, HttpStatus.OK);
    }

    @GetMapping("/{veiculoId}")
    public ResponseEntity<VeiculoDTO> obterVeiculo(@PathVariable String veiculoId) {
        Optional<VeiculoDTO> veiculo = veiculoService.obterVeiculoPorId(veiculoId);
        return veiculo.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletarTodosOsVeiculos() {
        veiculoService.deletarTodosOsVeiculos();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
