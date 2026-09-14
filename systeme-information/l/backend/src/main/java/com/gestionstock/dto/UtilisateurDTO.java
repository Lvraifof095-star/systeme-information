package com.gestionstock.dto;

import lombok.Data;

@Data
public class UtilisateurDTO {
    private String nom;
    private String prenom;
    private String email;
    private String motPasse; // uniquement rempli a la creation, ou lors d'une reinitialisation
    private boolean actif = true;
    private Long idRole;
}