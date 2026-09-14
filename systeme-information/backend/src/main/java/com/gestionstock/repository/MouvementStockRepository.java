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

    @Query("SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m " +
           "WHERE m.typeMouvement = :type AND m.date = :date")
    long sommeQuantiteParTypeEtDate(@Param("type") TypeMouvement type, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m " +
           "WHERE m.typeMouvement = 'SORTIE' AND m.date BETWEEN :debut AND :fin")
    double sommeSortiesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    List<MouvementStock> findTop10ByOrderByDateDescHeureDesc();
}
