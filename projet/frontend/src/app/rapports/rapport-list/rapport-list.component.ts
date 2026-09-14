import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { RapportService } from '../rapport.service';

@Component({
  selector: 'app-rapport-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rapport-list.component.html',
})
export class RapportListComponent {
  // periode par defaut : le mois en cours, pour le rapport des mouvements
  debut = this.premierJourDuMois();
  fin = this.aujourdHui();

  enCours = false;
  erreur = '';

  constructor(private rapportService: RapportService) {}

  telechargerProduitsExcel(): void {
    this.telecharger(this.rapportService.produitsExcel(), 'rapport_produits.xlsx');
  }

  telechargerProduitsPdf(): void {
    this.telecharger(this.rapportService.produitsPdf(), 'rapport_produits.pdf');
  }

  telechargerStockExcel(): void {
    this.telecharger(this.rapportService.stockExcel(), 'rapport_stock.xlsx');
  }

  telechargerStockPdf(): void {
    this.telecharger(this.rapportService.stockPdf(), 'rapport_stock.pdf');
  }

  telechargerFournisseursExcel(): void {
    this.telecharger(this.rapportService.fournisseursExcel(), 'rapport_fournisseurs.xlsx');
  }

  telechargerFournisseursPdf(): void {
    this.telecharger(this.rapportService.fournisseursPdf(), 'rapport_fournisseurs.pdf');
  }

  telechargerMouvementsExcel(): void {
    this.telecharger(this.rapportService.mouvementsExcel(this.debut, this.fin), 'rapport_mouvements.xlsx');
  }

  telechargerMouvementsPdf(): void {
    this.telecharger(this.rapportService.mouvementsPdf(this.debut, this.fin), 'rapport_mouvements.pdf');
  }

  private telecharger(source: Observable<Blob>, nomFichier: string): void {
    this.enCours = true;
    this.erreur = '';
    source.subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const lien = document.createElement('a');
        lien.href = url;
        lien.download = nomFichier;
        lien.click();
        window.URL.revokeObjectURL(url);
        this.enCours = false;
      },
      error: () => {
        this.enCours = false;
        this.erreur = 'Impossible de générer ce rapport.';
      },
    });
  }

  private aujourdHui(): string {
    return new Date().toISOString().slice(0, 10);
  }

  private premierJourDuMois(): string {
    const maintenant = new Date();
    return new Date(maintenant.getFullYear(), maintenant.getMonth(), 1).toISOString().slice(0, 10);
  }
}