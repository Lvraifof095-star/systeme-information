package com.gestionstock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "mouvement_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mouvement")
    private Long idMouvement;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement", nullable = false)
    private TypeMouvement typeMouvement;

    @Column(name = "sous_type", nullable = false, length = 30)
    private String sousType;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDate date;

    @Column(name = "heure_mouvement", nullable = false)
    private LocalTime heure;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_fournisseur")
    private Fournisseur fournisseur; // nullable, uniquement pour les entrees d'approvisionnement
}
