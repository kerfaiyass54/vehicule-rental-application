import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import Keycloak from 'keycloak-js';

export const roleRedirectGuard: CanActivateFn = async () => {
  const keycloak = inject(Keycloak);
  const router = inject(Router);

  const authenticated = keycloak.authenticated ?? false;

  if (!authenticated) {
    await keycloak.login({
      redirectUri: window.location.origin,
    });
    return false;
  }

  // Redirect based on role
  if (keycloak.hasRealmRole('supplier')) {
    return router.createUrlTree(['/supplier']);
  }
  return router.createUrlTree(['/forbidden']);
};
