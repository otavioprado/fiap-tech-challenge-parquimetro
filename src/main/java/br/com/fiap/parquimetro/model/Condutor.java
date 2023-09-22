package br.com.fiap.parquimetro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "condutores")
@Setter
@Getter
@Builder
public class Condutor {
    @Id
    private String id;
    private String nome;
    private String endereco;
    private String email;
    @DBRef
    private List<Veiculo> veiculos;
    @DBRef
    private FormaPagamento formaPagamento;
}
