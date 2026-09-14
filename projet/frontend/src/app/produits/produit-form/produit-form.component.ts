import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Categorie } from '../../models/produit.model';
import { CategorieService } from '../../categories/categorie.service';
import { ProduitService } from '../produit.service';

@Component({
  selector: 'app-produit-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './produit-form.component.html',
})
export class ProduitFormComponent implements OnInit {
  categories: Categorie[] = [];
  modeEdition = false;
  idProduit?: number;
  enregistrement = false;
  erreur = '';

  formulaire!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private produitService: ProduitService,
    private categorieService: CategorieService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.formulaire = this.fb.group({
      reference: ['', Validators.required],
      designation: ['', Validators.required],
      prixUnitaire: [0, [Validators.required, Validators.min(0)]],
      seuilAlerte: [0, [Validators.required, Validators.min(0)]],
      codeBarre: [''],
      idCategorie: [null as number | null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.categorieService.lister().subscribe((data) => (this.categories = data));

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.modeEdition = true;
      this.idProduit = Number(idParam);
      this.produitService.trouver(this.idProduit).subscribe((produit) => {
        this.formulaire.patchValue({
          reference: produit.reference,
          designation: produit.designation,
          prixUnitaire: produit.prixUnitaire,
          seuilAlerte: produit.seuilAlerte,
          codeBarre: produit.codeBarre ?? '',
          idCategorie: produit.categorie?.idCategorie ?? null,
        });
      });
    }
  }

  enregistrer(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const valeurs = this.formulaire.value;
    const produit = {
      reference: valeurs.reference!,
      designation: valeurs.designation!,
      prixUnitaire: valeurs.prixUnitaire!,
      seuilAlerte: valeurs.seuilAlerte!,
      codeBarre: valeurs.codeBarre || undefined,
      categorie: { idCategorie: valeurs.idCategorie! } as Categorie,
    };

    this.enregistrement = true;
    this.erreur = '';

    const requete = this.modeEdition
      ? this.produitService.modifier(this.idProduit!, produit)
      : this.produitService.ajouter(produit);

    requete.subscribe({
      next: () => this.router.navigate(['/produits']),
      error: () => {
        this.enregistrement = false;
        this.erreur = "Une erreur est survenue lors de l'enregistrement. Vérifiez que la référence n'est pas déjà utilisée.";
      },
    });
  }

  annuler(): void {
    this.router.navigate(['/produits']);
  }
}
