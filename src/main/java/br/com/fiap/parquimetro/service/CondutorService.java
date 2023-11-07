package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CondutorService {

    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;

    public Condutor registrarCondutor(Condutor condutor) {
        // Verificar se já existe um condutor com o mesmo email
        if (condutorRepository.existsByEmail(condutor.getEmail())) {
            throw new DataIntegrityViolationException("Já existe um condutor com o mesmo email: " + condutor.getEmail());
        }
        return condutorRepository.save(condutor);
    }

    public Condutor atualizarCondutor(Condutor condutor) {
        return condutorRepository.save(condutor);
    }

    public Optional<Condutor> obterCondutorPorId(String condutorId) {
        return condutorRepository.findById(condutorId);
    }

    public ResponseEntity<List<Veiculo>> obterVeiculosDoCondutor(String condutorId) {
        log.info("Consultando Veiculos do Condutor{}...", condutorId);

        Optional<Condutor> condutor = obterCondutorPorId(condutorId);

        if (condutor.isPresent()) {
            return new ResponseEntity<>(condutor.get().getVeiculos(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<Condutor> listarTodosOsCondutores() {
        return condutorRepository.findAll();
    }

    public void deletarTodosOsCondutores() {
        condutorRepository.deleteAll();
    }

    public ResponseEntity<String> vincularVeiculoAoCondutor(String condutorId, String veiculoId) {
        log.info("Vinculando Veiculo ao Condutor{}...",condutorId);
        Optional<Condutor> condutorOptional = condutorRepository.findById(condutorId);
        Optional<Veiculo> veiculoOptional = veiculoRepository.findById(veiculoId);

        if (condutorOptional.isPresent() && veiculoOptional.isPresent()) {
            Condutor condutor = condutorOptional.get();
            Veiculo veiculo = veiculoOptional.get();

            // Verifique se a lista de veículos não é nula e inicialize-a se for.
            if (condutor.getVeiculos() == null) {
                condutor.setVeiculos(new ArrayList<>());
            }

            condutor.getVeiculos().add(veiculo);
            condutorRepository.save(condutor);
            return new ResponseEntity<>("Veículo vinculado ao condutor com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Condutor ou Veículo não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
}