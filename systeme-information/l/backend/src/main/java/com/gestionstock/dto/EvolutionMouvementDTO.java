package com.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvolutionMouvementDTO {
    private String date;
    private long entrees;
    private long sorties;
}
