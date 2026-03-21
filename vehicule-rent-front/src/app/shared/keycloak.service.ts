import { Injectable , ChangeDetectionStrategy} from '@angular/core';

import { KeycloakProfile } from 'keycloak-js';
import {getKeycloak} from "./keycloak-init";
import {SessionService} from "./session-service";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  constructor(private sessionService: SessionService) {}


  async isLoggedIn(): Promise<boolean> {
    return !!getKeycloak().authenticated;
  }


  login(): void {
    getKeycloak().login();
  }

  logout(): void {
    getKeycloak().logout();
  }

  async loadUserProfile(): Promise<KeycloakProfile> {
    return  await getKeycloak().loadUserProfile();
  }

  getRoles(): string[] {
    const kc = getKeycloak();

    const realmRoles =
      kc.realmAccess?.roles ? kc.realmAccess.roles : [];
    const client = kc.clientId || 'angular-frontend';
    const clientRoles =
      kc.resourceAccess?.[client]?.roles
        ? kc.resourceAccess[client].roles
        : [];

    return [...realmRoles, ...clientRoles];
  }


  getToken(): string | undefined {
    return getKeycloak().token;
  }


}
