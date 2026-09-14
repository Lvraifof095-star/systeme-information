import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Produit } from '../models/produit.model';

@Injectable({ providedIn: 'root' })
export class ProduitService {
  private readonly url = `${environment.apiUrl}/produits`;

  constructor(private http: HttpClient) {}

  lister(): Observable<Produit[]> {
    return this.http.get<Produit[]>(this.url);
  }

  trouver(id: number): Observable<Produit> {
    return this.http.get<Produit>(`${this.url}/${id}`);
  }

  rechercher(motCle: string): Observable<Produit[]> {
    return this.http.get<Produit[]>(`${this.url}/recherche`, { params: { motCle } });
  }

  prochesDeLaRupture(): Observable<Produit[]> {
    return this.http.get<Produit[]>(`${this.url}/proches-rupture`);
  }

  ajouter(produit: Produit): Observable<Produit> {
    return this.http.post<Produit>(this.url, produit);
  }

  modifier(id: number, produit: Produit): Observable<Produit> {
    return this.http.put<Produit>(`${this.url}/${id}`, produit);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
