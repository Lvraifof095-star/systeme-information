package com.gestionstock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long idProduit;

    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Column(nullable = false, length = 150)
    private String designation;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "seuil_alerte")
    private Integer seuilAlerte = 0;

    @Column(name = "code_barre", length = 50)
    private String codeBarre;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    @OneToOne(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;
}
