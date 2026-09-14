package com.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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
    private double tauxDisponibilite;
    private List<ProduitUtilisationDTO> produitsPlusUtilises;
    private List<ProduitUtilisationDTO> produitsMoinsUtilises;
    private Double delaiMoyenReapprovisionnementJours; // null si aucune donnee disponible
}