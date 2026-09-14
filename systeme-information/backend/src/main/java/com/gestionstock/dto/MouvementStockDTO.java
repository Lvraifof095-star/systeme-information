package com.gestionstock.dto;

import com.gestionstock.entity.TypeMouvement;
import lombok.Data;

@Data
public class MouvementStockDTO {
    private Long idProduit;
    private TypeMouvement typeMouvement;
    private String sousType;
    private Integer quantite;
    private Long idFournisseur; // optionnel, uniquement pour un approvisionnement
}
