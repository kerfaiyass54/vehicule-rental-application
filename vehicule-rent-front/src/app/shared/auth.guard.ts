
import { CanActivateFn} from '@angular/router';
import {getKeycloak} from "./keycloak-init";


export const authGuard: CanActivateFn = () => {
  const kc = getKeycloak();

  if (!kc.authenticated) {
    kc.login();
    return false;
  }
  return true;
};
