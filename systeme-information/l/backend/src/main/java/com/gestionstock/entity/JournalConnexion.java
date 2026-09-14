package com.gestionstock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "journal_connexion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalConnexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_journal")
    private Long idJournal;

    @Column(name = "date_connexion", nullable = false)
    private LocalDateTime dateConnexion;

    @Column(name = "adresse_ip", length = 45)
    private String adresseIp;

    @Column(length = 20)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
