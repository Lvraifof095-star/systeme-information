package com.gestionstock.service;

import com.gestionstock.dto.KpiDTO;
import com.gestionstock.entity.TypeMouvement;
import com.gestionstock.repository.AlerteRepository;
import com.gestionstock.repository.MouvementStockRepository;
import com.gestionstock.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementRepository;
    private final AlerteRepository alerteRepository;

    public KpiDTO calculerKpi() {
        long nombreTotalProduits = produitRepository.count();
        double valeurTotaleStock = produitRepository.findAll().stream()
                .filter(p -> p.getStock() != null)
                .mapToDouble(p -> p.getPrixUnitaire() * p.getStock().getQuantiteActuelle())
                .sum();

        long produitsEnRupture = produitRepository.countProduitsEnRupture();
        double tauxDeRupture = nombreTotalProduits == 0
                ? 0
                : (produitsEnRupture * 100.0) / nombreTotalProduits;

        LocalDate debut = LocalDate.now().minusMonths(1);
        LocalDate fin = LocalDate.now();
        double consommation = mouvementRepository.sommeSortiesEntre(debut, fin);
        double stockMoyen = valeurTotaleStock; // simplifie : a affiner avec un historique de stock
        double tauxDeRotation = stockMoyen == 0 ? 0 : consommation / stockMoyen;

        long entreesDuJour = mouvementRepository.sommeQuantiteParTypeEtDate(TypeMouvement.ENTREE, LocalDate.now());
        long sortiesDuJour = mouvementRepository.sommeQuantiteParTypeEtDate(TypeMouvement.SORTIE, LocalDate.now());
        long produitsProchesDeLaRupture = produitRepository.findProduitsProchesDeLaRupture().size();

        return new KpiDTO(
                nombreTotalProduits,
                valeurTotaleStock,
                tauxDeRupture,
                tauxDeRotation,
                entreesDuJour,
                sortiesDuJour,
                produitsEnRupture,
                produitsProchesDeLaRupture
        );
    }
}
