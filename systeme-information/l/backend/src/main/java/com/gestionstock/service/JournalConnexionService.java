package com.gestionstock.service;

import com.gestionstock.entity.JournalConnexion;
import com.gestionstock.entity.Utilisateur;
import com.gestionstock.repository.JournalConnexionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JournalConnexionService {

    private final JournalConnexionRepository journalConnexionRepository;

    public void enregistrer(Utilisateur utilisateur, String statut, HttpServletRequest request) {
        JournalConnexion journal = new JournalConnexion();
        journal.setUtilisateur(utilisateur);
        journal.setDateConnexion(LocalDateTime.now());
        journal.setStatut(statut);
        journal.setAdresseIp(request != null ? request.getRemoteAddr() : "N/A");
        journalConnexionRepository.save(journal);
    }
}
