package com.temnafesta.dto.countPedidos;

public class CountPedidosResponseDto {

    private Long total;
    private Long aguardandoInicio;
    private Long pagamentoPendente;

    public CountPedidosResponseDto(
            Long total,
            Long aguardandoInicio,
            Long pagamentoPendente
    ) {
        this.total = total;
        this.aguardandoInicio = aguardandoInicio;
        this.pagamentoPendente = pagamentoPendente;
    }

    public Long getTotal() {
        return total;
    }

    public Long getAguardandoInicio() {
        return aguardandoInicio;
    }

    public Long getPagamentoPendente() {
        return pagamentoPendente;
    }
}