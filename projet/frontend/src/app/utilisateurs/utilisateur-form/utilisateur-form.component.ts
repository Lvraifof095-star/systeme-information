import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Role } from '../../models/utilisateur.model';
import { RoleService } from '../role.service';
import { UtilisateurService } from '../utilisateur.service';

@Component({
  selector: 'app-utilisateur-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './utilisateur-form.component.html',
})
export class UtilisateurFormComponent implements OnInit {
  roles: Role[] = [];
  modeEdition = false;
  idUtilisateur?: number;
  enregistrement = false;
  erreur = '';

  formulaire: FormGroup;

  constructor(
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private roleService: RoleService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.formulaire = this.fb.group({
      nom: ['', Validators.required],
      prenom: [''],
      email: ['', [Validators.required, Validators.email]],
      motPasse: [''],
      actif: [true],
      idRole: [null as number | null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.roleService.lister().subscribe((data) => (this.roles = data));

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.modeEdition = true;
      this.idUtilisateur = Number(idParam);
      this.utilisateurService.lister().subscribe((liste) => {
        const utilisateur = liste.find((u) => u.idUtilisateur === this.idUtilisateur);
        if (utilisateur) {
          this.formulaire.patchValue({
            nom: utilisateur.nom,
            prenom: utilisateur.prenom ?? '',
            email: utilisateur.email,
            actif: utilisateur.actif,
            idRole: utilisateur.role?.idRole ?? null,
          });
        }
      });
    }
  }

  enregistrer(): void {
    if (!this.modeEdition && !this.formulaire.value.motPasse) {
      this.erreur = 'Le mot de passe est obligatoire à la création.';
      return;
    }
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const valeurs = this.formulaire.value;
    const dto = {
      nom: valeurs.nom!,
      prenom: valeurs.prenom || undefined,
      email: valeurs.email!,
      motPasse: valeurs.motPasse || undefined,
      actif: valeurs.actif!,
      idRole: valeurs.idRole!,
    };

    this.enregistrement = true;
    this.erreur = '';

    const requete = this.modeEdition
      ? this.utilisateurService.modifier(this.idUtilisateur!, dto)
      : this.utilisateurService.ajouter(dto);

    requete.subscribe({
      next: () => this.router.navigate(['/utilisateurs']),
      error: (err) => {
        this.enregistrement = false;
        this.erreur = err?.error?.message || "Une erreur est survenue (email déjà utilisé ?).";
      },
    });
  }

  annuler(): void {
    this.router.navigate(['/utilisateurs']);
  }
}