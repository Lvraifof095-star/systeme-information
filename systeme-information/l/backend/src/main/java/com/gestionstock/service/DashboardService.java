package com.gestionstock.service;

import com.gestionstock.dto.EvolutionMouvementDTO;
import com.gestionstock.dto.KpiDTO;
import com.gestionstock.dto.ProduitUtilisationDTO;
import com.gestionstock.dto.RepartitionCategorieDTO;
import com.gestionstock.entity.TypeMouvement;
import com.gestionstock.repository.AlerteRepository;
import com.gestionstock.repository.MouvementStockRepository;
import com.gestionstock.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int LIMITE_CLASSEMENT = 5;

    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementRepository;
    private final AlerteRepository alerteRepository;

    public KpiDTO calculerKpi() {
        long nombreTotalProduits = produitRepository.count();

        double valeurTotaleStock = 0;
        long quantiteTotaleStock = 0;
        for (var produit : produitRepository.findAll()) {
            if (produit.getStock() != null) {
                int quantite = produit.getStock().getQuantiteActuelle();
                valeurTotaleStock += produit.getPrixUnitaire() * quantite;
                quantiteTotaleStock += quantite;
            }
        }

        long produitsEnRupture = produitRepository.countProduitsEnRupture();
        double tauxDeRupture = nombreTotalProduits == 0
                ? 0
                : (produitsEnRupture * 100.0) / nombreTotalProduits;

        double tauxDisponibilite = nombreTotalProduits == 0
                ? 0
                : ((nombreTotalProduits - produitsEnRupture) * 100.0) / nombreTotalProduits;

        LocalDate debut = LocalDate.now().minusMonths(1);
        LocalDate fin = LocalDate.now();
        double consommation = mouvementRepository.sommeSortiesEntre(debut, fin);

        // Taux de rotation = quantite consommee / quantite moyenne en stock.
        // Les deux termes doivent être dans la même unité (quantité), pas
        // quantité vs valeur monétaire — c'est le point corrigé ici.
        double tauxDeRotation = quantiteTotaleStock == 0 ? 0 : consommation / quantiteTotaleStock;

        long entreesDuJour = mouvementRepository.sommeQuantiteParTypeEtDate(TypeMouvement.ENTREE, LocalDate.now());
        long sortiesDuJour = mouvementRepository.sommeQuantiteParTypeEtDate(TypeMouvement.SORTIE, LocalDate.now());
        long produitsProchesDeLaRupture = produitRepository.findProduitsProchesDeLaRupture().size();

        List<ProduitUtilisationDTO> classement = classerProduitsParUtilisation(debut, fin);
        List<ProduitUtilisationDTO> produitsPlusUtilises = classement.stream()
                .limit(LIMITE_CLASSEMENT)
                .collect(Collectors.toList());
        List<ProduitUtilisationDTO> produitsMoinsUtilises = classement.stream()
                .sorted((a, b) -> Long.compare(a.getQuantiteSortie(), b.getQuantiteSortie()))
                .limit(LIMITE_CLASSEMENT)
                .collect(Collectors.toList());

        Double delaiMoyenReapprovisionnementJours = calculerDelaiMoyenReapprovisionnement();

        return new KpiDTO(
                nombreTotalProduits,
                valeurTotaleStock,
                tauxDeRupture,
                tauxDeRotation,
                entreesDuJour,
                sortiesDuJour,
                produitsEnRupture,
                produitsProchesDeLaRupture,
                tauxDisponibilite,
                produitsPlusUtilises,
                produitsMoinsUtilises,
                delaiMoyenReapprovisionnementJours
        );
    }

    private Double calculerDelaiMoyenReapprovisionnement() {
        List<Object[]> dates = mouvementRepository.datesApprovisionnement();
        if (dates.isEmpty()) {
            return null; // pas encore assez de donnees pour calculer ce KPI
        }

        long totalJours = 0;
        for (Object[] ligne : dates) {
            LocalDate dateReception = (LocalDate) ligne[0];
            LocalDate dateCommande = (LocalDate) ligne[1];
            totalJours += ChronoUnit.DAYS.between(dateCommande, dateReception);
        }
        return totalJours / (double) dates.size();
    }

    private List<ProduitUtilisationDTO> classerProduitsParUtilisation(LocalDate debut, LocalDate fin) {
        List<Object[]> resultats = produitRepository.classementUtilisationProduits(TypeMouvement.SORTIE, debut, fin);
        List<ProduitUtilisationDTO> classement = new ArrayList<>();
        for (Object[] ligne : resultats) {
            classement.add(new ProduitUtilisationDTO(
                    (String) ligne[0],
                    (String) ligne[1],
                    ((Number) ligne[2]).longValue()
            ));
        }
        return classement;
    }

    public List<EvolutionMouvementDTO> calculerEvolutionMouvements(int jours) {
        LocalDate fin = LocalDate.now();
        LocalDate debut = fin.minusDays(jours - 1L);
        List<Object[]> resultats = mouvementRepository.sommeParDateEtType(debut, fin);

        Map<LocalDate, long[]> parJour = new HashMap<>(); // index 0 = entrees, 1 = sorties
        for (Object[] ligne : resultats) {
            LocalDate date = (LocalDate) ligne[0];
            TypeMouvement type = (TypeMouvement) ligne[1];
            long quantite = ((Number) ligne[2]).longValue();
            long[] valeurs = parJour.computeIfAbsent(date, d -> new long[2]);
            if (type == TypeMouvement.ENTREE) {
                valeurs[0] += quantite;
            } else {
                valeurs[1] += quantite;
            }
        }

        List<EvolutionMouvementDTO> resultat = new ArrayList<>();
        for (int i = 0; i < jours; i++) {
            LocalDate jour = debut.plusDays(i);
            long[] valeurs = parJour.getOrDefault(jour, new long[2]);
            resultat.add(new EvolutionMouvementDTO(jour.toString(), valeurs[0], valeurs[1]));
        }
        return resultat;
    }

    public List<RepartitionCategorieDTO> calculerRepartitionCategories() {
        return produitRepository.compterParCategorie().stream()
                .map(ligne -> new RepartitionCategorieDTO((String) ligne[0], ((Number) ligne[1]).longValue()))
                .collect(Collectors.toList());
    }
}