import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-no-role',
  standalone: true,
  templateUrl: './no-role.html',
  styleUrl: './no-role.css',
})
export class NoRole {

  private keycloak = inject(Keycloak);
  private router = inject(Router);

  username = this.keycloak.tokenParsed?.["preferred_username"] ?? '';

  selectRole(role: string) {
    console.log('Requested role:', role);


    alert(`Request sent for ${role} role`);

    this.router.navigate(['/']);
  }

  logout() {
    this.keycloak.logout({
      redirectUri: window.location.origin
    });
  }

}
