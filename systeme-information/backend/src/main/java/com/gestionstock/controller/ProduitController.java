package com.gestionstock.controller;

import com.gestionstock.entity.Produit;
import com.gestionstock.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public List<Produit> lister() {
        return produitService.listerTous();
    }

    @GetMapping("/{id}")
    public Produit trouver(@PathVariable Long id) {
        return produitService.trouverParId(id);
    }

    @GetMapping("/recherche")
    public List<Produit> rechercher(@RequestParam String motCle) {
        return produitService.rechercher(motCle);
    }

    @GetMapping("/proches-rupture")
    public List<Produit> prochesDeLaRupture() {
        return produitService.produitsProchesDeLaRupture();
    }

    @PostMapping
    public Produit ajouter(@RequestBody Produit produit) {
        return produitService.ajouter(produit);
    }

    @PutMapping("/{id}")
    public Produit modifier(@PathVariable Long id, @RequestBody Produit produit) {
        return produitService.modifier(id, produit);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        produitService.supprimer(id);
    }
}
