package br.com.fiap.parquimetro.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "veiculos")
@Getter
@Setter
@Builder
public class Veiculo {
    @Id
    private String id;
    private String modelo;
    private String placa;
}
