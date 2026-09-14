import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Fournisseur } from '../models/produit.model';
import { FournisseurService } from './fournisseur.service';

@Component({
  selector: 'app-fournisseur-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './fournisseur-list.component.html',
})
export class FournisseurListComponent implements OnInit {
  fournisseurs: Fournisseur[] = [];
  chargement = false;
  erreur = '';

  constructor(private fournisseurService: FournisseurService, private router: Router) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.fournisseurService.lister().subscribe({
      next: (data) => {
        this.fournisseurs = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });
  }

  ajouter(): void {
    this.router.navigate(['/fournisseurs/nouveau']);
  }

  modifier(id?: number): void {
    if (!id) return;
    this.router.navigate(['/fournisseurs', id, 'modifier']);
  }

  supprimer(id?: number): void {
    if (!id) return;
    if (!confirm('Supprimer ce fournisseur ?')) return;
    this.fournisseurService.supprimer(id).subscribe({
      next: () => this.charger(),
      error: () =>
        (this.erreur = 'Impossible de supprimer ce fournisseur : des mouvements y sont probablement encore rattachés.'),
    });
  }
}