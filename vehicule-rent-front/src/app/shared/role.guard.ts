import { CanActivateFn, ActivatedRouteSnapshot } from '@angular/router';
import {getKeycloak} from "./keycloak-init";


export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const kc = getKeycloak();
  const requiredRoles = route.data['roles'] as string[] | undefined;
  const userRoles = kc.realmAccess?.roles ?? [];

  if (!requiredRoles || requiredRoles.length === 0) {
    return true; // no restriction
  }

  return requiredRoles.some(role => userRoles.includes(role));
};

