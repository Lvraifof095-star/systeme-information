import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Role } from '../models/utilisateur.model';

@Injectable({ providedIn: 'root' })
export class RoleService {
  private readonly url = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

  lister(): Observable<Role[]> {
    return this.http.get<Role[]>(this.url);
  }
}