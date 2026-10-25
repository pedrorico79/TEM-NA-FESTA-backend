package com.temnafesta.domain.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/** Relógio de referência para datas e horários de negócio no Brasil. */
public final class RelogioNegocio {

    public static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

    private RelogioNegocio() {
    }

    public static Clock sistema() {
        return Clock.system(FUSO);
    }

    public static LocalDateTime agora() {
        return LocalDateTime.now(sistema());
    }

    public static LocalDate hoje() {
        return LocalDate.now(sistema());
    }
}
