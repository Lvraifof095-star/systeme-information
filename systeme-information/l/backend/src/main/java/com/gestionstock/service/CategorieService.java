package com.gestionstock.service;

import com.gestionstock.entity.Categorie;
import com.gestionstock.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public List<Categorie> listerTous() {
        return categorieRepository.findAll();
    }

    public Categorie ajouter(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie modifier(Long id, Categorie donnees) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie introuvable"));
        categorie.setNomCategorie(donnees.getNomCategorie());
        return categorieRepository.save(categorie);
    }

    public void supprimer(Long id) {
        categorieRepository.deleteById(id);
    }
}
