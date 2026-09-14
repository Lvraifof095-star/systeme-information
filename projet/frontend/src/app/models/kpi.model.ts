export interface ProduitUtilisation {
  reference: string;
  designation: string;
  quantiteSortie: number;
}

export interface Kpi {
  nombreTotalProduits: number;
  valeurTotaleStock: number;
  tauxDeRupture: number;
  tauxDeRotation: number;
  entreesDuJour: number;
  sortiesDuJour: number;
  produitsEnRupture: number;
  produitsProchesDeLaRupture: number;
  tauxDisponibilite: number;
  produitsPlusUtilises: ProduitUtilisation[];
  produitsMoinsUtilises: ProduitUtilisation[];
  delaiMoyenReapprovisionnementJours: number | null;
}

export interface EvolutionMouvement {
  date: string;
  entrees: number;
  sorties: number;
}

export interface RepartitionCategorie {
  nomCategorie: string;
  nombreProduits: number;
}