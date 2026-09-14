import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Categorie } from '../models/produit.model';

@Injectable({ providedIn: 'root' })
export class CategorieService {
  private readonly url = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient) {}

  lister(): Observable<Categorie[]> {
    return this.http.get<Categorie[]>(this.url);
  }

  ajouter(categorie: Categorie): Observable<Categorie> {
    return this.http.post<Categorie>(this.url, categorie);
  }

  modifier(id: number, categorie: Categorie): Observable<Categorie> {
    return this.http.put<Categorie>(`${this.url}/${id}`, categorie);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}