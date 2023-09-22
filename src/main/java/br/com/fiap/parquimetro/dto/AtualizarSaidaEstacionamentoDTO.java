package br.com.fiap.parquimetro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AtualizarSaidaEstacionamentoDTO {
    private LocalDateTime saida;
}