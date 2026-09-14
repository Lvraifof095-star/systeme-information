package com.gestionstock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerte")
    private Long idAlerte;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_alerte", nullable = false)
    private TypeAlerte typeAlerte;

    @Column(name = "date_generation", nullable = false)
    private LocalDateTime dateGeneration;

    @Column(length = 20)
    private String statut = "NON_TRAITEE";

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;
}
