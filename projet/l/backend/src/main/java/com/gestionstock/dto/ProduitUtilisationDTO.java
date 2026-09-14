package com.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProduitUtilisationDTO {
    private String reference;
    private String designation;
    private long quantiteSortie;
}