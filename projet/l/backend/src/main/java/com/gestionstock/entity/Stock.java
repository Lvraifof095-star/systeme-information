package com.gestionstock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;

    @Column(name = "quantite_actuelle", nullable = false)
    private Integer quantiteActuelle = 0;

    @OneToOne
    @JoinColumn(name = "id_produit", unique = true)
    @JsonIgnore // evite la boucle infinie Produit -> Stock -> Produit lors de la serialisation JSON
    private Produit produit;
}