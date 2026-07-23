package com.temnafesta.dto.countPedidos;

public class CountPedidosResponseDto {

    private Long pedidosAtivos;
    private Long aguardandoPreparo;
    private Long emProducao;
    private Long pagamentosPendentes;

    public CountPedidosResponseDto(
            Long pedidosAtivos,
            Long aguardandoPreparo,
            Long emProducao,
            Long pagamentosPendentes
    ) {
        this.pedidosAtivos = pedidosAtivos;
        this.aguardandoPreparo = aguardandoPreparo;
        this.emProducao = emProducao;
        this.pagamentosPendentes = pagamentosPendentes;
    }

    public Long getPedidosAtivos() {
        return pedidosAtivos;
    }

    public Long getAguardandoPreparo() {
        return aguardandoPreparo;
    }

    public Long getEmProducao() {
        return emProducao;
    }

    public Long getPagamentosPendentes() {
        return pagamentosPendentes;
    }
}