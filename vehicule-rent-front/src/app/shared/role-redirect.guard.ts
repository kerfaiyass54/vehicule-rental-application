import {CanActivateFn, Router} from '@angular/router';
import { getKeycloak } from './keycloak-init';
import {SessionService} from "./session-service";
import {inject} from "@angular/core";

export const roleRedirectGuard: CanActivateFn = (route, state) => {
  const kc = getKeycloak();
  const router = inject(Router);

  if (!kc.authenticated) {
    kc.login();
    return false;
  }

  const realmRoles = kc.realmAccess?.roles ?? [];
  const clientRoles =
    kc.resourceAccess?.[kc.clientId || 'angular-frontend']?.roles ?? [];

  const roles = [...realmRoles, ...clientRoles];

  // 🔥 NEW USER → NO ROLE
  if (roles.length === 0) {
    router.navigate(['/select-role']);
    return false;
  }

  if (roles.includes('admin')) {
    router.navigate(['/admin']);
    return false;
  }

  if (roles.includes('supplier')) {
    router.navigate(['/supplier']);
    return false;
  }

  if (roles.includes('repairer')) {
    router.navigate(['/repair']);
    return false;
  }

  if (roles.includes('client')) {
    router.navigate(['/client']);
    return false;
  }

  router.navigate(['/not-authorized']);
  return false;
};
