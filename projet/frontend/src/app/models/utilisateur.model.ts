export interface Role {
  idRole: number;
  nomRole: string;
}

export interface Utilisateur {
  idUtilisateur?: number;
  nom: string;
  prenom?: string;
  email: string;
  actif: boolean;
  role?: Role;
}

export interface UtilisateurDTO {
  nom: string;
  prenom?: string;
  email: string;
  motPasse?: string;
  actif: boolean;
  idRole: number;
}