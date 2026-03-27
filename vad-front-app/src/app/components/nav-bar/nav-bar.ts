import {Component, inject, Input} from '@angular/core';
import {NavItem} from '../models/NavItem';
import Keycloak from 'keycloak-js';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/list';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {MatTooltip} from '@angular/material/tooltip';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-nav-bar',
  imports: [
    MatToolbar,
    MatIcon,
    MatDivider,
    RouterLink,
    RouterLinkActive,
    MatTooltip,
    MatButton
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css',
})
export class NavBar {

  @Input() navItems: NavItem[] = [];
  @Input() brandName: string = 'VAD';
  @Input() brandIcon: string = 'directions_car';

  private keycloak = inject(Keycloak);

  get username(): string {
    return this.keycloak.tokenParsed?.['preferred_username'] ?? '';
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

}
