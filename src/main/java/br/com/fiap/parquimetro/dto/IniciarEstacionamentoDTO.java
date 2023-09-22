package br.com.fiap.parquimetro.dto;

import br.com.fiap.parquimetro.enums.TipoPeriodoEstacionamento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IniciarEstacionamentoDTO {
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private TipoPeriodoEstacionamento tipo;
    private String condutorId;
    private String veiculoId;
}
