import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthResponse {
  token: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly url = `${environment.apiUrl}/auth`;
  private readonly tokenKey = 'gestion_stock_token';
  private readonly roleKey = 'gestion_stock_role';

  constructor(private http: HttpClient) {}

  login(email: string, motPasse: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/login`, { email, motPasse }).pipe(
      tap((response) => {
        sessionStorage.setItem(this.tokenKey, response.token);
        sessionStorage.setItem(this.roleKey, response.role);
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.roleKey);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  getRole(): string | null {
    return sessionStorage.getItem(this.roleKey);
  }

  isAuthentifie(): boolean {
    return !!this.getToken();
  }
}
