package br.com.fiap.parquimetro.model;

import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "estacionamentos")
@Getter
@Setter
public class Estacionamento {
    @Id
    private String id;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private TipoPeriodoEstacionamento tipo;
    @DBRef
    private Condutor condutor;
    @DBRef
    private Veiculo veiculo;
    @Null
    @DBRef
    private FormaPagamento pagamento;
    @Null
    private double valor;
}

