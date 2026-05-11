import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID); 

  if (isPlatformBrowser(platformId)) {
    const token = localStorage.getItem('token');
    const perfilCompletado = localStorage.getItem('perfilCompletado') === 'true';
    
    if (token) {
      if (!perfilCompletado && state.url !== '/completar-perfil') {
        router.navigate(['/completar-perfil']);
        return false;
      }
      return true; 
    }
  }

  router.navigate(['/login']);
  return false;
};