package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.dto.FormaPagamentoDTO;
import br.com.fiap.parquimetro.mapper.FormaPagamentoMapper;
import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.FormaPagamento;
import br.com.fiap.parquimetro.repository.CondutorRepository;
import br.com.fiap.parquimetro.repository.FormaPagamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormaPagamentoService {

    private final CondutorRepository condutorRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final FormaPagamentoMapper formaPagamentoMapper;

    public FormaPagamentoDTO criarFormaPagamento(FormaPagamentoDTO formaPagamentoDTO) {
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
        return formaPagamentoMapper.toDTO(novaFormaPagamento);
    }

    public Optional<FormaPagamento> obterFormaPagamento(String id) {
        return formaPagamentoRepository.findById(id);
    }

    public List<FormaPagamentoDTO> findAll() {
        return formaPagamentoRepository.findAll()
                .stream()
                .map(formaPagamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        formaPagamentoRepository.deleteAll();
    }
}
