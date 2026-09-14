import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Produit } from '../../models/produit.model';
import { ProduitService } from '../produit.service';

@Component({
  selector: 'app-produit-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './produit-list.component.html',
})
export class ProduitListComponent implements OnInit {
  produits: Produit[] = [];
  motCle = '';
  chargement = false;

  constructor(private produitService: ProduitService, private router: Router) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.produitService.lister().subscribe({
      next: (data) => {
        this.produits = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });
  }

  rechercher(): void {
    if (!this.motCle.trim()) {
      this.charger();
      return;
    }
    this.produitService.rechercher(this.motCle).subscribe((data) => (this.produits = data));
  }

  supprimer(id?: number): void {
    if (!id) return;
    if (!confirm('Confirmer la suppression de ce produit ?')) return;
    this.produitService.supprimer(id).subscribe(() => this.charger());
  }

  estEnAlerte(produit: Produit): boolean {
    const quantite = produit.stock?.quantiteActuelle ?? 0;
    return quantite <= produit.seuilAlerte;
  }

  ajouter(): void {
    this.router.navigate(['/produits/nouveau']);
  }

  modifier(id?: number): void {
    if (!id) return;
    this.router.navigate(['/produits', id, 'modifier']);
  }
}
