package com.gestionstock.repository;

import com.gestionstock.entity.Produit;
import com.gestionstock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByDesignationContainingIgnoreCase(String motCle);

    @Query("SELECT p FROM Produit p WHERE p.stock.quantiteActuelle <= p.seuilAlerte AND p.stock.quantiteActuelle > 0")
    List<Produit> findProduitsProchesDeLaRupture();

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.stock.quantiteActuelle = 0")
    long countProduitsEnRupture();

    @Query("SELECT p.categorie.nomCategorie, COUNT(p) FROM Produit p GROUP BY p.categorie.nomCategorie")
    List<Object[]> compterParCategorie();

    // Classement des produits par quantite sortie sur une periode (utilise pour
    // les KPI "produits les plus/moins utilises"). Les produits sans aucune
    // sortie apparaissent avec une quantite de 0 grace au LEFT JOIN.
    @Query("SELECT p.reference, p.designation, " +
            "COALESCE(SUM(CASE WHEN m.typeMouvement = :type AND m.date BETWEEN :debut AND :fin THEN m.quantite ELSE 0 END), 0) " +
            "FROM Produit p LEFT JOIN MouvementStock m ON m.produit = p " +
            "GROUP BY p.idProduit, p.reference, p.designation " +
            "ORDER BY 3 DESC")
    List<Object[]> classementUtilisationProduits(
            @Param("type") TypeMouvement type,
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}