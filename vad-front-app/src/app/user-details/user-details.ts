// user-details.component.ts  – replace the relevant parts
import { Component, inject, OnInit, signal } from '@angular/core';
import { UserInfo } from './models/UserInfo';
import Keycloak from 'keycloak-js';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatBadgeModule } from '@angular/material/badge';
import { DatePipe } from '@angular/common';
import { MatTab, MatTabGroup, MatTabLabel } from '@angular/material/tabs';
import {MatButton} from '@angular/material/button';
import { SmartTableColumn } from '../components/models/SmartTableColumn';
import { TableFetchParams } from '../components/models/TableFetchParams';
import { TableData } from '../components/table-data/table-data';
import {SessionService} from '../services/session-service';
import {Session} from '../models/Session';
import {UpdateProfileModal} from './update-profile-modal/update-profile-modal';
import {UpdatePasswordModal} from './update-password-modal/update-password-modal';
import {DeleteAccountModal} from './delete-account-modal/delete-account-modal';



@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [
    MatCardModule, MatIconModule, MatDividerModule, MatChipsModule,
    MatProgressSpinnerModule, MatTableModule, MatBadgeModule,
    DatePipe, MatTabGroup, MatTab, MatTabLabel, MatButton, TableData,UpdateProfileModal, UpdatePasswordModal, DeleteAccountModal,
  ],
  templateUrl: './user-details.html',
  styleUrl:    './user-details.css',
})
export class UserDetails implements OnInit {

  protected keycloak        = inject(Keycloak);
  private sessionService  = inject(SessionService);

  userInfo = signal<UserInfo | null>(null);
  sessions = signal<Session[]>([]);
     showUpdateProfile  = signal(false);
   showUpdatePassword = signal(false);
   showDeleteAccount  = signal(false);

  // ── Table state ────────────────────────────────────────────────
  columns: SmartTableColumn[] = [
    { key: 'username',         label: 'User',         icon: '👤', type: 'avatar', sortable: true  },
    { key: 'email',            label: 'Email',        icon: '📧',                 sortable: true  },
    { key: 'ipAddress',        label: 'IP Address',   icon: '🌐',                 sortable: false },
    { key: 'deviceType',       label: 'Device',       icon: '💻', type: 'badge',  sortable: true  },
    { key: 'city',             label: 'Location',     icon: '📍',                 sortable: true  },
    { key: 'riskScore',        label: 'Risk Score',   icon: '⚠️', type: 'progress', sortable: true },
    { key: 'suspicious',       label: 'Suspicious',   icon: '🚨', type: 'badge',  sortable: true  },
    { key: 'sessionStart',     label: 'Started At',   icon: '🕐',                 sortable: true  },
  ];

  rows: Record<string, unknown>[] = [];
  total = 0;
  tableLoading = false;
  loading = false;

  // current user email from token (used to query sessions)
  private userEmail = '';

  // ── Lifecycle ──────────────────────────────────────────────────
  ngOnInit(): void {
    const token = this.keycloak.tokenParsed;

    if (token) {
      this.userEmail = token['email'] ?? '';
      this.userInfo.set({
        username:      token['preferred_username'] ?? '',
        email:         this.userEmail,
        firstName:     token['given_name']         ?? '',
        lastName:      token['family_name']        ?? '',
        roles:         this.keycloak.realmAccess?.roles ?? [],
        emailVerified: token['email_verified']     ?? false,
      });
    }

    // Load first page immediately
    this.loadSessions({ page: 0, size: 10, sortField: '', sortDir: '', filter: '' });
  }

  // ── Table events ───────────────────────────────────────────────
  onFetch(params: TableFetchParams): void {
    this.loadSessions(params);
  }

  onRowClick(row: Record<string, unknown>): void {
    console.log('Session clicked:', row);
  }

  // ── Private ────────────────────────────────────────────────────
  private loadSessions(params: TableFetchParams): void {
    this.tableLoading = true;

    this.sessionService
      .findByEmailPaged(this.userEmail, params.page, params.size)
      .subscribe({
        next: (res) => {
          this.rows  = res.content.map(s => this.toRow(s));
          this.total = res.totalElements;
          this.tableLoading = false;
        },
        error: () => {
          this.tableLoading = false;
        },
      });
  }

  private toRow(s: Session): Record<string, unknown> {
    return {
      id:              s.id,
      username:        s.username,
      email:           s.email,
      ipAddress:       s.ipAddress,
      deviceType:      s.deviceType  || 'Unknown',
      city:            s.city && s.country ? `${s.city}, ${s.country}` : (s.city || s.country || '—'),
      riskScore:       Math.round(s.riskScore),   // 0-100 for the progress bar
      suspicious:      s.suspicious ? 'Yes' : 'No',
      sessionStart:    s.sessionStart
        ? new Date(s.sessionStart).toLocaleString()
        : '—',
    };
  }

  // ── Account actions ────────────────────────────────────────────
  onUpdate(updatedUser?: Partial<UserInfo>): void {
    this.showUpdateProfile.set(true);
  }

  onUpdatePassword(): void {
    this.showUpdatePassword.set(true);
  }

  onDeleteAccount(): void {
    this.showDeleteAccount.set(true);
  }
}
