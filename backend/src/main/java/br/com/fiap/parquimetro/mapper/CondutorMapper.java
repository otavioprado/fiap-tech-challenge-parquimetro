package br.com.fiap.parquimetro.mapper;

import br.com.fiap.parquimetro.dto.CondutorDTO;
import br.com.fiap.parquimetro.model.Condutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CondutorMapper {
    Condutor toEntity(CondutorDTO dto);

    @Mapping(target = "formaPagamentoId", source = "formaPagamento.id")
    CondutorDTO toDTO(Condutor entity);
}
