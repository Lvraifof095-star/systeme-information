import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { Categorie } from '../models/produit.model';
import { CategorieService } from './categorie.service';

@Component({
  selector: 'app-categorie-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorie list.component.html',
})
export class CategorieListComponent implements OnInit {
  categories: Categorie[] = [];
  chargement = false;
  erreur = '';

  nouvelleCategorie = '';
  ajoutEnCours = false;

  idEnEdition: number | null = null;
  valeurEdition = '';

  constructor(private categorieService: CategorieService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.categorieService.lister().subscribe({
      next: (data) => {
        this.categories = data;
        this.chargement = false;
      },
      error: () => {
        this.chargement = false;
        this.erreur = 'Impossible de charger les catégories. Vérifiez que le serveur est démarré.';
      },
    });
  }

  ajouter(): void {
    const nom = this.nouvelleCategorie.trim();
    if (!nom) {
      this.erreur = 'Saisissez le nom de la catégorie avant de cliquer sur Ajouter.';
      return;
    }

    this.ajoutEnCours = true;
    this.erreur = '';
    this.categorieService
      .ajouter({ nomCategorie: nom })
      .pipe(finalize(() => (this.ajoutEnCours = false)))
      .subscribe({
        next: () => {
          this.nouvelleCategorie = '';
          this.charger();
        },
        error: () => {
          this.erreur = "Impossible d'ajouter cette catégorie. Vérifiez que le serveur est démarré et que le nom n'est pas déjà utilisé.";
        },
      });
  }

  commencerEdition(categorie: Categorie): void {
    this.idEnEdition = categorie.idCategorie ?? null;
    this.valeurEdition = categorie.nomCategorie;
  }

  annulerEdition(): void {
    this.idEnEdition = null;
    this.valeurEdition = '';
  }

  enregistrerEdition(id?: number): void {
    if (!id || !this.valeurEdition.trim()) return;
    this.categorieService.modifier(id, { nomCategorie: this.valeurEdition.trim() }).subscribe({
      next: () => {
        this.annulerEdition();
        this.charger();
      },
      error: () => (this.erreur = 'Impossible de modifier cette catégorie.'),
    });
  }

  supprimer(id?: number): void {
    if (!id) return;
    if (!confirm('Supprimer cette catégorie ? Les produits associés devront être réassignés.')) return;
    this.categorieService.supprimer(id).subscribe({
      next: () => this.charger(),
      error: () =>
        (this.erreur = 'Impossible de supprimer cette catégorie : des produits y sont probablement encore rattachés.'),
    });
  }
}