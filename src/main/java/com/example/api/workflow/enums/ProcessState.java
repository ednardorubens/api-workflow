package com.example.api.workflow.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessState {

    STARTED("Iniciado"),
    UNDER_REVIEW("Em Análise"),
    APROVED("Aprovado"),
    REPROVED("Reprovado");

    private final String description;
}
