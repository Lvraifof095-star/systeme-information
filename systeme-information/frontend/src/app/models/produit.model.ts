export interface Categorie {
  idCategorie?: number;
  nomCategorie: string;
}

export interface Stock {
  idStock?: number;
  quantiteActuelle: number;
}

export interface Produit {
  idProduit?: number;
  reference: string;
  designation: string;
  prixUnitaire: number;
  seuilAlerte: number;
  codeBarre?: string;
  categorie?: Categorie;
  stock?: Stock;
}

export interface Fournisseur {
  idFournisseur?: number;
  nom: string;
  contact?: string;
  adresse?: string;
}