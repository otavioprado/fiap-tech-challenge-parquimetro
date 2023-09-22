package br.com.fiap.parquimetro.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CondutorDTO {
    private String id;
    private String nome;
    private String endereco;
    private String email;
    private String formaPagamentoId;
}
