export type TypeAlerte = 'RUPTURE' | 'STOCK_FAIBLE' | 'DEPASSEMENT_SEUIL';

export interface Alerte {
  idAlerte: number;
  typeAlerte: TypeAlerte;
  dateGeneration: string;
  statut: string;
  produit?: { idProduit: number; reference: string; designation: string };
}