package com.gestionstock.controller;

import com.gestionstock.entity.Categorie;
import com.gestionstock.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public List<Categorie> lister() {
        return categorieService.listerTous();
    }

    @PostMapping
    public Categorie ajouter(@RequestBody Categorie categorie) {
        return categorieService.ajouter(categorie);
    }

    @PutMapping("/{id}")
    public Categorie modifier(@PathVariable Long id, @RequestBody Categorie categorie) {
        return categorieService.modifier(id, categorie);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        categorieService.supprimer(id);
    }
}
