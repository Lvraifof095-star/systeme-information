import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthentifie() && authService.getRole() === 'ADMINISTRATEUR') {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};