import { Routes } from '@angular/router';
import { adminGuard } from './core/admin.guard';
import { authGuard } from './core/auth.guard';
import { AlerteListComponent } from './alerte/Alerte-list.component';
import { LoginComponent } from './auth/login/login.component';
import { CategorieListComponent } from './categories/categorie list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FournisseurFormComponent } from './fournisseurs/fournisseur-form.component';
import { FournisseurListComponent } from './fournisseurs/fournisseur-list.component';
import { MouvementFormComponent } from './mouvements/mouvement-form.component';
import { MouvementRecentsComponent } from './mouvements/mouvement-recents.component';
import { ProduitFormComponent } from './produits/produit-form/produit-form.component';
import { ProduitListComponent } from './produits/produit-list/produit-list.component';
import { RapportListComponent } from './rapports/rapport-list/rapport-list.component';
import { UtilisateurFormComponent } from './utilisateurs/utilisateur-form/utilisateur-form.component';
import { UtilisateurListComponent } from './utilisateurs/utilisateur-list/utilisateur-list.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'produits', component: ProduitListComponent, canActivate: [authGuard] },
  { path: 'produits/nouveau', component: ProduitFormComponent, canActivate: [authGuard] },
  { path: 'produits/:id/modifier', component: ProduitFormComponent, canActivate: [authGuard] },
  { path: 'mouvements', component: MouvementRecentsComponent, canActivate: [authGuard] },
  { path: 'mouvements/nouveau', component: MouvementFormComponent, canActivate: [authGuard] },
  { path: 'categories', component: CategorieListComponent, canActivate: [authGuard, adminGuard] },
  { path: 'fournisseurs', component: FournisseurListComponent, canActivate: [authGuard, adminGuard] },
  { path: 'fournisseurs/nouveau', component: FournisseurFormComponent, canActivate: [authGuard, adminGuard] },
  { path: 'fournisseurs/:id/modifier', component: FournisseurFormComponent, canActivate: [authGuard, adminGuard] },
  { path: 'alertes', component: AlerteListComponent, canActivate: [authGuard] },
  { path: 'utilisateurs', component: UtilisateurListComponent, canActivate: [authGuard, adminGuard] },
  { path: 'utilisateurs/nouveau', component: UtilisateurFormComponent, canActivate: [authGuard, adminGuard] },
  { path: 'utilisateurs/:id/modifier', component: UtilisateurFormComponent, canActivate: [authGuard, adminGuard] },
  { path: 'rapports', component: RapportListComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
];