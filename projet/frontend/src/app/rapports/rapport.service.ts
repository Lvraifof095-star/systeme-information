import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RapportService {
  private readonly url = `${environment.apiUrl}/rapports`;

  constructor(private http: HttpClient) {}

  produitsExcel(): Observable<Blob> {
    return this.http.get(`${this.url}/produits/excel`, { responseType: 'blob' });
  }

  produitsPdf(): Observable<Blob> {
    return this.http.get(`${this.url}/produits/pdf`, { responseType: 'blob' });
  }

  stockExcel(): Observable<Blob> {
    return this.http.get(`${this.url}/stock/excel`, { responseType: 'blob' });
  }

  stockPdf(): Observable<Blob> {
    return this.http.get(`${this.url}/stock/pdf`, { responseType: 'blob' });
  }

  fournisseursExcel(): Observable<Blob> {
    return this.http.get(`${this.url}/fournisseurs/excel`, { responseType: 'blob' });
  }

  fournisseursPdf(): Observable<Blob> {
    return this.http.get(`${this.url}/fournisseurs/pdf`, { responseType: 'blob' });
  }

  mouvementsExcel(debut: string, fin: string): Observable<Blob> {
    return this.http.get(`${this.url}/mouvements/excel`, { params: { debut, fin }, responseType: 'blob' });
  }

  mouvementsPdf(debut: string, fin: string): Observable<Blob> {
    return this.http.get(`${this.url}/mouvements/pdf`, { params: { debut, fin }, responseType: 'blob' });
  }
}