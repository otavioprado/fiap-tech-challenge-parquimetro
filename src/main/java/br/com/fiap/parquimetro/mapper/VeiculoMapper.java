package br.com.fiap.parquimetro.mapper;

import br.com.fiap.parquimetro.dto.VeiculoDTO;
import br.com.fiap.parquimetro.model.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VeiculoMapper {
    Veiculo toEntity(VeiculoDTO dto);

    VeiculoDTO toDTO(Veiculo entity);
}
