import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Utilisateur, UtilisateurDTO } from '../models/utilisateur.model';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private readonly url = `${environment.apiUrl}/utilisateurs`;

  constructor(private http: HttpClient) {}

  lister(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(this.url);
  }

  ajouter(dto: UtilisateurDTO): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(this.url, dto);
  }

  modifier(id: number, dto: UtilisateurDTO): Observable<Utilisateur> {
    return this.http.put<Utilisateur>(`${this.url}/${id}`, dto);
  }

  reinitialiserMotPasse(id: number, nouveauMotPasse: string): Observable<void> {
    return this.http.put<void>(`${this.url}/${id}/reinitialiser-mot-passe`, { nouveauMotPasse });
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}