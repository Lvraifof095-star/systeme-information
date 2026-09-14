import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FournisseurService } from './fournisseur.service';

@Component({
  selector: 'app-fournisseur-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './fournisseur-form.component.html',
})
export class FournisseurFormComponent implements OnInit {
  modeEdition = false;
  idFournisseur?: number;
  enregistrement = false;
  erreur = '';

  formulaire!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.formulaire = this.fb.group({
      nom: ['', Validators.required],
      contact: [''],
      adresse: [''],
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.modeEdition = true;
      this.idFournisseur = Number(idParam);
      this.fournisseurService.lister().subscribe((liste) => {
        const fournisseur = liste.find((f) => f.idFournisseur === this.idFournisseur);
        if (fournisseur) {
          this.formulaire.patchValue({
            nom: fournisseur.nom,
            contact: fournisseur.contact ?? '',
            adresse: fournisseur.adresse ?? '',
          });
        }
      });
    }
  }

  enregistrer(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const valeurs = this.formulaire.value;
    const fournisseur = {
      nom: valeurs.nom!,
      contact: valeurs.contact || undefined,
      adresse: valeurs.adresse || undefined,
    };

    this.enregistrement = true;
    this.erreur = '';

    const requete = this.modeEdition
      ? this.fournisseurService.modifier(this.idFournisseur!, fournisseur)
      : this.fournisseurService.ajouter(fournisseur);

    requete.subscribe({
      next: () => this.router.navigate(['/fournisseurs']),
      error: () => {
        this.enregistrement = false;
        this.erreur = "Une erreur est survenue lors de l'enregistrement.";
      },
    });
  }

  annuler(): void {
    this.router.navigate(['/fournisseurs']);
  }
}