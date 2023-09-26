package br.com.fiap.parquimetro.service;

import br.com.fiap.parquimetro.dto.VeiculoDTO;
import br.com.fiap.parquimetro.mapper.VeiculoMapper;
import br.com.fiap.parquimetro.model.Veiculo;
import br.com.fiap.parquimetro.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;

    public Veiculo registrarVeiculo(VeiculoDTO veiculoDTO) {
        // Verificar se já existe um veículo com a mesma placa
        if (veiculoRepository.existsByPlaca(veiculoDTO.getPlaca())) {
            throw new DataIntegrityViolationException("Já existe um veículo com a mesma placa " + veiculoDTO.getPlaca());
        }

        Veiculo veiculo = veiculoMapper.toEntity(veiculoDTO);
        return veiculoRepository.save(veiculo);
    }

    public Optional<VeiculoDTO> obterVeiculoPorId(String veiculoId) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(veiculoId);
        return veiculo.map(veiculoMapper::toDTO);
    }

    public List<VeiculoDTO> listarTodosOsVeiculos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos.stream()
                .map(veiculoMapper::toDTO)
                .toList();
    }

    public void deletarTodosOsVeiculos() {
        veiculoRepository.deleteAll();
    }
}




