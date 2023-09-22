package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CondutorService {
    private final CondutorRepository condutorRepository;

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

    public List<Condutor> listarTodosOsCondutores() {
        return condutorRepository.findAll();
    }

    public void deletarTodosOsCondutores() {
        condutorRepository.deleteAll();
    }
}