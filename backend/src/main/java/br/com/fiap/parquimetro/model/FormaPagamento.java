package br.com.fiap.parquimetro.model;

import br.com.fiap.parquimetro.enums.TipoFormaPagamento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "formaPagamento")
@Setter
@Getter
@Builder
public class FormaPagamento {
    @Id
    private String id;
    private String descricao;
    private TipoFormaPagamento tipo;
    private String condutorId;
}
