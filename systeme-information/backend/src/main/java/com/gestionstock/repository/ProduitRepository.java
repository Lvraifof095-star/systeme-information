package com.gestionstock.repository;

import com.gestionstock.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByDesignationContainingIgnoreCase(String motCle);

    @Query("SELECT p FROM Produit p WHERE p.stock.quantiteActuelle <= p.seuilAlerte AND p.stock.quantiteActuelle > 0")
    List<Produit> findProduitsProchesDeLaRupture();

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.stock.quantiteActuelle = 0")
    long countProduitsEnRupture();
}
