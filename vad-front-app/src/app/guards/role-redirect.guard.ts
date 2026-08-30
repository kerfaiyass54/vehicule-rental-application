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

  // role-based redirect
  if (keycloak.hasRealmRole('supplier')) {
    return router.createUrlTree(['/supplier']);
  }

  if (keycloak.hasRealmRole('admin')) {
    return router.createUrlTree(['/admin']);
  }

  if (keycloak.hasRealmRole('client')) {
    return router.createUrlTree(['/client']);
  }

  if (keycloak.hasRealmRole('repair')) {
    return router.createUrlTree(['/repair']);
  }

  // 👇 NEW behavior
  return router.createUrlTree(['/no-role']);
};
