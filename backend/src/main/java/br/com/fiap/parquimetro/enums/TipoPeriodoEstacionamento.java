package br.com.fiap.parquimetro.enums;

public enum TipoPeriodoEstacionamento {
    PERIODO_FIXO("Período Fixo"),
    POR_HORA("Por Hora");

    private final String descricao;

    TipoPeriodoEstacionamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
