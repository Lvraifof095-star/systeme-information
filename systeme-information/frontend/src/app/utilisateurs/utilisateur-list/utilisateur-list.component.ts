import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Utilisateur } from '../../models/utilisateur.model';
import { UtilisateurService } from '../utilisateur.service';

@Component({
  selector: 'app-utilisateur-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './utilisateur-list.component.html',
})
export class UtilisateurListComponent implements OnInit {
  utilisateurs: Utilisateur[] = [];
  chargement = false;
  erreur = '';
  message = '';

  constructor(private utilisateurService: UtilisateurService, private router: Router) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.utilisateurService.lister().subscribe({
      next: (data) => {
        this.utilisateurs = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });
  }

  ajouter(): void {
    this.router.navigate(['/utilisateurs/nouveau']);
  }

  modifier(id?: number): void {
    if (!id) return;
    this.router.navigate(['/utilisateurs', id, 'modifier']);
  }

  supprimer(id?: number): void {
    if (!id) return;
    if (!confirm('Supprimer cet utilisateur ?')) return;
    this.utilisateurService.supprimer(id).subscribe({
      next: () => this.charger(),
      error: () => (this.erreur = 'Impossible de supprimer cet utilisateur.'),
    });
  }

  reinitialiserMotPasse(id?: number): void {
    if (!id) return;
    const nouveauMotPasse = prompt('Nouveau mot de passe pour cet utilisateur :');
    if (!nouveauMotPasse) return;

    this.utilisateurService.reinitialiserMotPasse(id, nouveauMotPasse).subscribe({
      next: () => {
        this.message = 'Mot de passe réinitialisé avec succès.';
        setTimeout(() => (this.message = ''), 3000);
      },
      error: () => (this.erreur = 'Impossible de réinitialiser le mot de passe.'),
    });
  }
}