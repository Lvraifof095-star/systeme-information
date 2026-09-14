import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Fournisseur } from '../models/produit.model';

@Injectable({ providedIn: 'root' })
export class FournisseurService {
  private readonly url = `${environment.apiUrl}/fournisseurs`;

  constructor(private http: HttpClient) {}

  lister(): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(this.url);
  }

  ajouter(fournisseur: Fournisseur): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(this.url, fournisseur);
  }

  modifier(id: number, fournisseur: Fournisseur): Observable<Fournisseur> {
    return this.http.put<Fournisseur>(`${this.url}/${id}`, fournisseur);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}