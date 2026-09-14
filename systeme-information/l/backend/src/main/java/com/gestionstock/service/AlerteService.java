package com.gestionstock.service;

import com.gestionstock.entity.Alerte;
import com.gestionstock.entity.Produit;
import com.gestionstock.entity.TypeAlerte;
import com.gestionstock.repository.AlerteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlerteService {

    private final AlerteRepository alerteRepository;

    public void genererAlerte(Produit produit, TypeAlerte type) {
        // Evite de dupliquer une alerte deja active du meme type pour le meme produit
        boolean dejaExistante = alerteRepository
                .findFirstByProduitAndTypeAlerteAndStatut(produit, type, "NON_TRAITEE")
                .isPresent();
        if (dejaExistante) {
            return;
        }
        Alerte alerte = new Alerte();
        alerte.setProduit(produit);
        alerte.setTypeAlerte(type);
        alerte.setDateGeneration(LocalDateTime.now());
        alerte.setStatut("NON_TRAITEE");
        alerteRepository.save(alerte);
    }

    public List<Alerte> listerNonTraitees() {
        return alerteRepository.findByStatut("NON_TRAITEE");
    }

    public Alerte traiter(Long id) {
        Alerte alerte = alerteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerte introuvable"));
        alerte.setStatut("TRAITEE");
        return alerteRepository.save(alerte);
    }
}
