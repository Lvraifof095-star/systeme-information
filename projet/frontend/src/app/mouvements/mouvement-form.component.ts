import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Fournisseur, Produit } from '../models/produit.model';
import { TypeMouvement } from '../models/mouvement.model';
import { FournisseurService } from '../fournisseurs/fournisseur.service';
import { ProduitService } from '../produits/produit.service';
import { MouvementService } from './mouvement.service';

@Component({
  selector: 'app-mouvement-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './mouvement-form.component.html',
})
export class MouvementFormComponent implements OnInit {
  produits: Produit[] = [];
  fournisseurs: Fournisseur[] = [];
  enregistrement = false;
  erreur = '';
  succes = '';

  sousTypesEntree = ['Reception', 'Approvisionnement', 'Inventaire'];
  sousTypesSortie = ['Vente', 'Perte', 'Casse', 'ConsommationInterne', 'Transfert'];

  formulaire!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private produitService: ProduitService,
    private fournisseurService: FournisseurService,
    private mouvementService: MouvementService,
    private router: Router
  ) {
    this.formulaire = this.fb.group({
      typeMouvement: ['ENTREE' as TypeMouvement, Validators.required],
      sousType: ['Reception', Validators.required],
      idProduit: [null as number | null, Validators.required],
      quantite: [1, [Validators.required, Validators.min(1)]],
      idFournisseur: [null as number | null],
      dateCommande: [null as string | null],
    });
  }

  ngOnInit(): void {
    this.produitService.lister().subscribe((data) => (this.produits = data));
    this.fournisseurService.lister().subscribe((data) => (this.fournisseurs = data));

    this.formulaire.get('typeMouvement')?.valueChanges.subscribe((type) => {
      const premierSousType = type === 'ENTREE' ? this.sousTypesEntree[0] : this.sousTypesSortie[0];
      this.formulaire.patchValue({ sousType: premierSousType, idFournisseur: null, dateCommande: null });
    });
  }

  get sousTypesDisponibles(): string[] {
    return this.formulaire.value.typeMouvement === 'ENTREE' ? this.sousTypesEntree : this.sousTypesSortie;
  }

  get estApprovisionnement(): boolean {
    return this.formulaire.value.typeMouvement === 'ENTREE' && this.formulaire.value.sousType === 'Approvisionnement';
  }

  enregistrer(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const valeurs = this.formulaire.value;
    this.enregistrement = true;
    this.erreur = '';
    this.succes = '';

    this.mouvementService
      .enregistrer({
        idProduit: valeurs.idProduit!,
        typeMouvement: valeurs.typeMouvement!,
        sousType: valeurs.sousType!,
        quantite: valeurs.quantite!,
        idFournisseur: this.estApprovisionnement ? valeurs.idFournisseur ?? undefined : undefined,
        dateCommande: this.estApprovisionnement ? valeurs.dateCommande ?? undefined : undefined,
      })
      .subscribe({
        next: () => {
          this.enregistrement = false;
          this.succes = 'Mouvement enregistré avec succès.';
          this.formulaire.patchValue({ quantite: 1 });
        },
        error: (err) => {
          this.enregistrement = false;
          this.erreur =
            err?.error?.message || "Impossible d'enregistrer ce mouvement (stock insuffisant ou données invalides).";
        },
      });
  }

  annuler(): void {
    this.router.navigate(['/produits']);
  }
}