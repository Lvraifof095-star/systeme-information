package com.gestionstock.controller;

import com.gestionstock.dto.ReinitialisationMotPasseDTO;
import com.gestionstock.dto.UtilisateurDTO;
import com.gestionstock.entity.Utilisateur;
import com.gestionstock.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public List<Utilisateur> lister() {
        return utilisateurService.listerTous();
    }

    @PostMapping
    public Utilisateur ajouter(@RequestBody UtilisateurDTO dto) {
        return utilisateurService.ajouter(dto);
    }

    @PutMapping("/{id}")
    public Utilisateur modifier(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        return utilisateurService.modifier(id, dto);
    }

    @PutMapping("/{id}/reinitialiser-mot-passe")
    public void reinitialiserMotPasse(@PathVariable Long id, @RequestBody ReinitialisationMotPasseDTO dto) {
        utilisateurService.reinitialiserMotPasse(id, dto);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        utilisateurService.supprimer(id);
    }
}