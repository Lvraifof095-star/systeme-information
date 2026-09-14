package com.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepartitionCategorieDTO {
    private String nomCategorie;
    private long nombreProduits;
}