package com.gestionstock.security;

import com.gestionstock.entity.Utilisateur;
import com.gestionstock.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));

        String role = utilisateur.getRole() != null ? utilisateur.getRole().getNomRole() : "OPERATEUR";

        return new User(
                utilisateur.getEmail(),
                utilisateur.getMotPasse(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        );
    }
}
