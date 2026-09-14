package com.gestionstock.repository;

import com.gestionstock.entity.MouvementStock;
import com.gestionstock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    List<MouvementStock> findByProduit_IdProduitOrderByDateDescHeureDesc(Long idProduit);

    List<MouvementStock> findByProduit_IdProduitAndUtilisateurOrderByDateDescHeureDesc(
            Long idProduit, com.gestionstock.entity.Utilisateur utilisateur);

    @Query("SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m " +
            "WHERE m.typeMouvement = :type AND m.date = :date")
    long sommeQuantiteParTypeEtDate(@Param("type") TypeMouvement type, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m " +
            "WHERE m.typeMouvement = 'SORTIE' AND m.date BETWEEN :debut AND :fin")
    double sommeSortiesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT m.date, m.typeMouvement, COALESCE(SUM(m.quantite), 0) FROM MouvementStock m " +
            "WHERE m.date BETWEEN :debut AND :fin GROUP BY m.date, m.typeMouvement")
    List<Object[]> sommeParDateEtType(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    List<MouvementStock> findTop10ByOrderByDateDescHeureDesc();

    List<MouvementStock> findTop10ByUtilisateurOrderByDateDescHeureDesc(
            com.gestionstock.entity.Utilisateur utilisateur);

    // Renvoie les couples (date de reception, date de commande) pour les
    // entrees de type Approvisionnement ayant une date de commande renseignee,
    // utilise pour calculer le KPI "delai moyen de reapprovisionnement".
    @Query("SELECT m.date, m.dateCommande FROM MouvementStock m " +
            "WHERE m.typeMouvement = 'ENTREE' AND m.sousType = 'Approvisionnement' " +
            "AND m.dateCommande IS NOT NULL")
    List<Object[]> datesApprovisionnement();
}