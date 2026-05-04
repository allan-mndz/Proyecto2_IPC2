import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID); // Herramienta para saber dónde estamos

  // Preguntamos si estamos corriendo dentro de un navegador real?
  if (isPlatformBrowser(platformId)) {
    const token = localStorage.getItem('token');
    
    // Si hay token, lo dejamos pasar
    if (token) {
      return true; 
    }
  }

  // Si no es un navegador o no hay token, lo regresamos al login
  router.navigate(['/login']);
  return false;
};