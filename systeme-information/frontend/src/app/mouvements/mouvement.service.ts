import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MouvementStock, MouvementStockDTO } from '../models/mouvement.model';

@Injectable({ providedIn: 'root' })
export class MouvementService {
  private readonly url = `${environment.apiUrl}/mouvements`;

  constructor(private http: HttpClient) {}

  enregistrer(dto: MouvementStockDTO): Observable<MouvementStock> {
    return this.http.post<MouvementStock>(this.url, dto);
  }

  recents(): Observable<MouvementStock[]> {
    return this.http.get<MouvementStock[]>(`${this.url}/recents`);
  }

  historique(idProduit: number): Observable<MouvementStock[]> {
    return this.http.get<MouvementStock[]>(`${this.url}/produit/${idProduit}`);
  }
}