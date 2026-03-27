import {Component, inject, OnInit, signal} from '@angular/core';
import {UserInfo} from './models/UserInfo';
import {Session} from './models/Session';
import Keycloak from 'keycloak-js';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatBadgeModule } from '@angular/material/badge';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-user-details',
  standalone:true,
  imports: [MatCardModule,
    MatIconModule,
    MatDividerModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatBadgeModule,
    DatePipe,],
  templateUrl: './user-details.html',
  styleUrl: './user-details.css',
})
export class UserDetails implements OnInit {
  private keycloak = inject(Keycloak);

  userInfo = signal<UserInfo | null>(null);
  sessions = signal<Session[]>([]);
  loading = signal(true);

  sessionColumns = ['status', 'ipAddress', 'browser', 'started', 'lastAccess'];

  ngOnInit(): void {
    const token = this.keycloak.tokenParsed;

    if (token) {
      this.userInfo.set({
        username:      token['preferred_username'] ?? '',
        email:         token['email'] ?? '',
        firstName:     token['given_name'] ?? '',
        lastName:      token['family_name'] ?? '',
        roles:         this.keycloak.realmAccess?.roles ?? [],
        emailVerified: token['email_verified'] ?? false,
      });
    }

    // Sessions come from Keycloak Account REST API
    // Replace with your actual Keycloak base URL and realm
    const { url, realm } = (this.keycloak as any)['authServerUrl']
      ? { url: (this.keycloak as any)['authServerUrl'], realm: (this.keycloak as any)['realm'] }
      : { url: 'http://localhost:8080', realm: 'your-realm' };

    fetch(`${url}/realms/${realm}/account/sessions`, {
      headers: { Authorization: `Bearer ${this.keycloak.token}` },
    })
      .then(res => res.json())
      .then((data: any[]) => {
        this.sessions.set(
          data.map((s, i) => ({
            id:         s.id,
            ipAddress:  s.ipAddress,
            started:    new Date(s.start),
            lastAccess: new Date(s.lastAccess),
            browser:    s.browser ?? 'Unknown',
            current:    i === 0,
          }))
        );
      })
      .catch(() => this.sessions.set([]))
      .finally(() => this.loading.set(false));
  }
}

