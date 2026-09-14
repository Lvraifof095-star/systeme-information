import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Alerte } from './Alerte.model';

@Injectable({ providedIn: 'root' })
export class AlerteService {
  private readonly url = `${environment.apiUrl}/alertes`;

  constructor(private http: HttpClient) {}

  listerNonTraitees(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(this.url);
  }

  traiter(id: number): Observable<Alerte> {
    return this.http.put<Alerte>(`${this.url}/${id}/traiter`, {});
  }
}