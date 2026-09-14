package com.gestionstock.repository;

import com.gestionstock.entity.Alerte;
import com.gestionstock.entity.Produit;
import com.gestionstock.entity.TypeAlerte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByStatut(String statut);
    Optional<Alerte> findFirstByProduitAndTypeAlerteAndStatut(Produit produit, TypeAlerte type, String statut);
}
