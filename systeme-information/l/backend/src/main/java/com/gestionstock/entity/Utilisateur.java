package com.gestionstock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(length = 80)
    private String prenom;

    @Column(unique = true, nullable = false, length = 120)
    private String email;

    @Column(name = "mot_passe", nullable = false)
    @JsonIgnore // le hash du mot de passe ne doit jamais partir dans une reponse API
    private String motPasse;

    @Column(nullable = false)
    private boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}