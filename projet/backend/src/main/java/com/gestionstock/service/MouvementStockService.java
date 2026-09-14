package com.gestionstock.service;

import com.gestionstock.dto.MouvementStockDTO;
import com.gestionstock.entity.*;
import com.gestionstock.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final StockRepository stockRepository;
    private final FournisseurRepository fournisseurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AlerteService alerteService;

    @Transactional
    public MouvementStock enregistrerMouvement(MouvementStockDTO dto) {
        Produit produit = produitRepository.findById(dto.getIdProduit())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        Stock stock = produit.getStock();

        if (dto.getTypeMouvement() == TypeMouvement.SORTIE
                && stock.getQuantiteActuelle() < dto.getQuantite()) {
            throw new IllegalStateException("Quantite en stock insuffisante");
        }

        int nouvelleQuantite = dto.getTypeMouvement() == TypeMouvement.ENTREE
                ? stock.getQuantiteActuelle() + dto.getQuantite()
                : stock.getQuantiteActuelle() - dto.getQuantite();
        stock.setQuantiteActuelle(nouvelleQuantite);
        stockRepository.save(stock);

        if (nouvelleQuantite == 0) {
            alerteService.genererAlerte(produit, TypeAlerte.RUPTURE);
        } else if (nouvelleQuantite <= produit.getSeuilAlerte()) {
            alerteService.genererAlerte(produit, TypeAlerte.STOCK_FAIBLE);
        }

        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduit(produit);
        mouvement.setUtilisateur(utilisateurCourant());
        mouvement.setTypeMouvement(dto.getTypeMouvement());
        mouvement.setSousType(dto.getSousType());
        mouvement.setQuantite(dto.getQuantite());
        mouvement.setDate(LocalDate.now());
        mouvement.setHeure(LocalTime.now());

        if (dto.getIdFournisseur() != null) {
            Fournisseur fournisseur = fournisseurRepository.findById(dto.getIdFournisseur())
                    .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));
            mouvement.setFournisseur(fournisseur);
        }

        return mouvementRepository.save(mouvement);
    }

    public List<MouvementStock> historiqueParProduit(Long idProduit) {
        return mouvementRepository.findByProduit_IdProduitOrderByDateDescHeureDesc(idProduit);
    }

    public List<MouvementStock> mouvementsRecents() {
        return mouvementRepository.findTop10ByOrderByDateDescHeureDesc();
    }

    private Utilisateur utilisateurCourant() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non authentifie"));
    }
}
