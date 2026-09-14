import { Injectable } from '@angular/core';

const CLE_STOCKAGE = 'gestion_stock_theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private themeActuel: 'clair' | 'sombre' = 'clair';

  constructor() {
    const enregistre = localStorage.getItem(CLE_STOCKAGE) as 'clair' | 'sombre' | null;
    const preferenceSysteme = window.matchMedia?.('(prefers-color-scheme: dark)').matches;
    this.themeActuel = enregistre ?? (preferenceSysteme ? 'sombre' : 'clair');
    this.appliquer();
  }

  get theme(): 'clair' | 'sombre' {
    return this.themeActuel;
  }

  basculer(): void {
    this.themeActuel = this.themeActuel === 'clair' ? 'sombre' : 'clair';
    localStorage.setItem(CLE_STOCKAGE, this.themeActuel);
    this.appliquer();
  }

  private appliquer(): void {
    document.documentElement.setAttribute('data-theme', this.themeActuel === 'sombre' ? 'sombre' : 'clair');
  }
}