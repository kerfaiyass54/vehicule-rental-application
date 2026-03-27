import { Component, Input, inject } from '@angular/core';
import { RouterModule, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import Keycloak from 'keycloak-js';
import {NavItem} from '../models/NavItem';
import {LogoutConfirmDialog} from '../logout-confirm-dialog/logout-confirm-dialog';


@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [
    RouterModule,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatDividerModule,
    MatDialogModule,
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css',
})
export class NavBar {
  @Input() navItems: NavItem[] = [];
  @Input() brandName: string = 'Rentals Manager';
  @Input() brandIcon: string = 'directions_car';
  @Input() role: string = '';

  private keycloak = inject(Keycloak);
  private dialog = inject(MatDialog);

  get username(): string {
    return this.keycloak.tokenParsed?.['preferred_username'] ?? '';
  }

  openLogoutDialog(): void {
    const dialogRef = this.dialog.open(LogoutConfirmDialog, {
      width: '360px',
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.keycloak.logout({ redirectUri: window.location.origin });
      }
    });
  }
}
