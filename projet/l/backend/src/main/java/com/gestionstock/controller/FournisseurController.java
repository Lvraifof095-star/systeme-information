package com.gestionstock.controller;

import com.gestionstock.entity.Fournisseur;
import com.gestionstock.service.FournisseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @GetMapping
    public List<Fournisseur> lister() {
        return fournisseurService.listerTous();
    }

    @PostMapping
    public Fournisseur ajouter(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.ajouter(fournisseur);
    }

    @PutMapping("/{id}")
    public Fournisseur modifier(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurService.modifier(id, fournisseur);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        fournisseurService.supprimer(id);
    }
}
