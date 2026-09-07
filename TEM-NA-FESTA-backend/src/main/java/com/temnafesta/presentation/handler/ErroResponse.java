package com.temnafesta.presentation.handler;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        List<String> detalhes
) {
    public ErroResponse(Integer status, String erro, List<String> detalhes) {
        this(LocalDateTime.now(), status, erro, detalhes);
    }
}