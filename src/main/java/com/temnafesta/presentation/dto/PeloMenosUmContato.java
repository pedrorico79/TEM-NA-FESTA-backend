package com.temnafesta.presentation.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeloMenosUmContatoValidator.class)
public @interface PeloMenosUmContato {

    String message() default "Pelo menos um meio de contato deve ser informado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}