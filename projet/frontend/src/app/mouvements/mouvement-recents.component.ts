import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MouvementStock } from '../models/mouvement.model';
import { MouvementService } from './mouvement.service';

@Component({
  selector: 'app-mouvement-recents',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mouvement-recents.component.html',
})
export class MouvementRecentsComponent implements OnInit {
  mouvements: MouvementStock[] = [];
  chargement = true;

  constructor(private mouvementService: MouvementService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement = true;
    this.mouvementService.recents().subscribe({
      next: (data) => {
        this.mouvements = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });
  }
}