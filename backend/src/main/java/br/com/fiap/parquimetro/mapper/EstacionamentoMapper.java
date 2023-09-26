package br.com.fiap.parquimetro.mapper;

import br.com.fiap.parquimetro.dto.IniciarEstacionamentoDTO;
import br.com.fiap.parquimetro.model.Condutor;
import br.com.fiap.parquimetro.model.Estacionamento;
import org.mapstruct.Mapper;

@Mapper
public interface EstacionamentoMapper {

    Estacionamento toEntity(IniciarEstacionamentoDTO dto);

    IniciarEstacionamentoDTO toDTO(Condutor entity);
}
