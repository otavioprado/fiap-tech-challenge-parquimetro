package br.com.fiap.parquimetro.dto;

import br.com.fiap.parquimetro.enums.TipoFormaPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoDTO {
    private String id;
    private String descricao;
    private TipoFormaPagamento tipo;
    private String condutorId;
}

