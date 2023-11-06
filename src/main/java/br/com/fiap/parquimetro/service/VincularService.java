package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VincularService {


    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;

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
