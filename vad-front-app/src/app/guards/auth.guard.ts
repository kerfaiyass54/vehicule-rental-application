import { inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import Keycloak from 'keycloak-js';

export const authGuard: CanActivateFn = async (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const keycloak = inject(Keycloak);
  const router = inject(Router);

  // Check if user is authenticated
  const authenticated = keycloak.authenticated ?? false;

  if (!authenticated) {
    await keycloak.login({
      redirectUri: window.location.origin + state.url,
    });
    return false;
  }

  // Check required roles if defined on the route
  const requiredRoles = route.data['roles'] as string[] | undefined;

  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }

  const hasRoles = requiredRoles.every(role =>
    keycloak.hasRealmRole(role)
  );

  if (!hasRoles) {
    router.navigate(['/forbidden']);
    return false;
  }

  return true;
};
