package com.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiDTO {
    private long nombreTotalProduits;
    private double valeurTotaleStock;
    private double tauxDeRupture;
    private double tauxDeRotation;
    private long entreesDuJour;
    private long sortiesDuJour;
    private long produitsEnRupture;
    private long produitsProchesDeLaRupture;
}
