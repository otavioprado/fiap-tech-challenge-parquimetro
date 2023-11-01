package br.com.fiap.parquimetro.mapper;

import br.com.fiap.parquimetro.dto.CondutorDTO;
import br.com.fiap.parquimetro.model.Condutor;
import org.mapstruct.Mapper;

@Mapper
public interface CondutorMapper {
    Condutor toEntity(CondutorDTO dto);

    CondutorDTO toDTO(Condutor entity);
}
