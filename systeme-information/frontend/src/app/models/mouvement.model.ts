export type TypeMouvement = 'ENTREE' | 'SORTIE';

export interface MouvementStockDTO {
  idProduit: number;
  typeMouvement: TypeMouvement;
  sousType: string;
  quantite: number;
  idFournisseur?: number;
  dateCommande?: string; // optionnel, uniquement pour un approvisionnement
}

export interface MouvementStock {
  idMouvement: number;
  typeMouvement: TypeMouvement;
  sousType: string;
  quantite: number;
  date: string;
  heure: string;
  produit?: { idProduit: number; reference: string; designation: string };
  utilisateur?: { idUtilisateur: number; nom: string; prenom?: string };
  fournisseur?: { idFournisseur: number; nom: string };
}