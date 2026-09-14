package com.gestionstock.service;

import com.gestionstock.entity.Fournisseur;
import com.gestionstock.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public List<Fournisseur> listerTous() {
        return fournisseurRepository.findAll();
    }

    public Fournisseur ajouter(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur modifier(Long id, Fournisseur donnees) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));
        fournisseur.setNom(donnees.getNom());
        fournisseur.setContact(donnees.getContact());
        fournisseur.setAdresse(donnees.getAdresse());
        return fournisseurRepository.save(fournisseur);
    }

    public void supprimer(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
