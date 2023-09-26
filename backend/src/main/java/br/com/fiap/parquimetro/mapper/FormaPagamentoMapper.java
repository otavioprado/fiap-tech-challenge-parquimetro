package br.com.fiap.parquimetro.mapper;

import br.com.fiap.parquimetro.dto.FormaPagamentoDTO;
import br.com.fiap.parquimetro.model.FormaPagamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FormaPagamentoMapper {

    FormaPagamentoDTO toDTO(FormaPagamento formaPagamento);

    FormaPagamento toEntity(FormaPagamentoDTO dto);
}

