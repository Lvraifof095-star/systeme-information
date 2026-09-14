package com.gestionstock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fournisseur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fournisseur")
    private Long idFournisseur;

    @Column(nullable = false, length = 120)
    private String nom;

    @Column(length = 80)
    private String contact;

    @Column(length = 200)
    private String adresse;
}
