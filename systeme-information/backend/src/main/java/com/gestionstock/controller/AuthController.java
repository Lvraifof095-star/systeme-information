package com.gestionstock.controller;

import com.gestionstock.dto.AuthResponse;
import com.gestionstock.dto.LoginRequest;
import com.gestionstock.entity.Utilisateur;
import com.gestionstock.repository.UtilisateurRepository;
import com.gestionstock.security.JwtUtil;
import com.gestionstock.service.JournalConnexionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JournalConnexionService journalConnexionService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));

        if (!passwordEncoder.matches(request.getMotPasse(), utilisateur.getMotPasse())) {
            journalConnexionService.enregistrer(utilisateur, "ECHEC", httpRequest);
            throw new BadCredentialsException("Identifiants invalides");
        }

        journalConnexionService.enregistrer(utilisateur, "SUCCES", httpRequest);
        String token = jwtUtil.genererToken(utilisateur);
        String role = utilisateur.getRole() != null ? utilisateur.getRole().getNomRole() : "OPERATEUR";
        return new AuthResponse(token, role);
    }
}
