package com.gestionstock.service;

import com.gestionstock.dto.ReinitialisationMotPasseDTO;
import com.gestionstock.dto.UtilisateurDTO;
import com.gestionstock.entity.Role;
import com.gestionstock.entity.Utilisateur;
import com.gestionstock.repository.RoleRepository;
import com.gestionstock.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Utilisateur> listerTous() {
        return utilisateurRepository.findAll();
    }

    @Transactional
    public Utilisateur ajouter(UtilisateurDTO dto) {
        if (dto.getMotPasse() == null || dto.getMotPasse().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire à la création.");
        }

        Role role = roleRepository.findById(dto.getIdRole())
                .orElseThrow(() -> new EntityNotFoundException("Rôle introuvable"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotPasse(passwordEncoder.encode(dto.getMotPasse()));
        utilisateur.setActif(dto.isActif());
        utilisateur.setRole(role);

        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur modifier(Long id, UtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        Role role = roleRepository.findById(dto.getIdRole())
                .orElseThrow(() -> new EntityNotFoundException("Rôle introuvable"));

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setActif(dto.isActif());
        utilisateur.setRole(role);
        // le mot de passe ne se modifie pas ici : voir reinitialiserMotPasse()

        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void reinitialiserMotPasse(Long id, ReinitialisationMotPasseDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (dto.getNouveauMotPasse() == null || dto.getNouveauMotPasse().isBlank()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide.");
        }

        utilisateur.setMotPasse(passwordEncoder.encode(dto.getNouveauMotPasse()));
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void supprimer(Long id) {
        utilisateurRepository.deleteById(id);
    }
}