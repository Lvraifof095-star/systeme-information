import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Alerte } from './Alerte.model';
import { AlerteService } from './Alerte.service';

@Component({
  selector: 'app-alerte-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alerte-list.component.html',
})
export class AlerteListComponent implements OnInit {
  alertes: Alerte[] = [];
  chargement = false;

  constructor(private alerteService: AlerteService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.alerteService.listerNonTraitees().subscribe({
      next: (data) => {
        this.alertes = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });
  }

  traiter(id: number): void {
    this.alerteService.traiter(id).subscribe(() => this.charger());
  }

  libelleType(type: string): string {
    switch (type) {
      case 'RUPTURE':
        return 'Rupture de stock';
      case 'STOCK_FAIBLE':
        return 'Stock faible';
      case 'DEPASSEMENT_SEUIL':
        return 'Dépassement de seuil';
      default:
        return type;
    }
  }
}