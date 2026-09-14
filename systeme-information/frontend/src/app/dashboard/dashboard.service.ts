import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Kpi as KpiModel } from '../models/kpi.model';

export interface Kpi {
  nombreTotalProduits: number;
  valeurTotaleStock: number;
  tauxDeRupture: number;
  tauxDeRotation: number;
  entreesDuJour: number;
  sortiesDuJour: number;
  produitsEnRupture: number;
  produitsProchesDeLaRupture: number;
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

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly url = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getKpi(): Observable<KpiModel> {
    return this.http.get<KpiModel>(`${this.url}/kpi`);
  }

  getEvolutionMouvements(jours: number): Observable<EvolutionMouvement[]> {
    return this.http.get<EvolutionMouvement[]>(`${this.url}/evolution-mouvements`, {
      params: { jours },
    });
  }

  getRepartitionCategories(): Observable<RepartitionCategorie[]> {
    return this.http.get<RepartitionCategorie[]>(`${this.url}/repartition-categories`);
  }
}