package com.gestionstock.service;

import com.gestionstock.entity.Produit;
import com.gestionstock.entity.Stock;
import com.gestionstock.repository.ProduitRepository;
import com.gestionstock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final StockRepository stockRepository;

    public List<Produit> listerTous() {
        return produitRepository.findAll();
    }

    public Produit trouverParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    public List<Produit> rechercher(String motCle) {
        return produitRepository.findByDesignationContainingIgnoreCase(motCle);
    }

    @Transactional
    public Produit ajouter(Produit produit) {
        Produit enregistre = produitRepository.save(produit);

        Stock stock = new Stock();
        stock.setProduit(enregistre);
        stock.setQuantiteActuelle(0);
        stockRepository.save(stock);

        enregistre.setStock(stock);
        return enregistre;
    }

    public Produit modifier(Long id, Produit donnees) {
        Produit produit = trouverParId(id);
        produit.setDesignation(donnees.getDesignation());
        produit.setPrixUnitaire(donnees.getPrixUnitaire());
        produit.setSeuilAlerte(donnees.getSeuilAlerte());
        produit.setCodeBarre(donnees.getCodeBarre());
        produit.setCategorie(donnees.getCategorie());
        return produitRepository.save(produit);
    }

    public void supprimer(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> produitsProchesDeLaRupture() {
        return produitRepository.findProduitsProchesDeLaRupture();
    }
}
