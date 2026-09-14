import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  email = '';
  motPasse = '';
  erreur = '';

  constructor(private authService: AuthService, private router: Router) {}

  seConnecter(): void {
    this.erreur = '';
    this.authService.login(this.email, this.motPasse).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => (this.erreur = 'Identifiants invalides'),
    });
  }
}
