import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';
import Keycloak from 'keycloak-js';

import {
  finalize,
  take
} from 'rxjs';
import {ClientService} from '../../../services/client-services/client.service';
import {Client} from '../../models/client.model';




@Component({
  selector: 'app-client-details',
  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './client-details.html',
  styleUrl: './client-details.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientDetails implements OnInit {

  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak = inject(Keycloak);

  private readonly clientService =
    inject(ClientService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // STATE
  // =========================================================

  readonly client =
    signal<Client | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly clientEmail =
    signal('');


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadLoggedInClient();
  }


  // =========================================================
  // LOAD LOGGED-IN CLIENT
  // =========================================================

  private loadLoggedInClient(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'];

    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.loading.set(false);
      this.error.set(true);

      this.cdr.markForCheck();

      return;
    }

    this.clientEmail.set(email);

    this.loadClient(email);
  }


  // =========================================================
  // LOAD CLIENT
  // =========================================================

  private loadClient(
    email: string
  ): void {

    this.loading.set(true);
    this.error.set(false);

    this.clientService
      .getClient(email)
      .pipe(
        take(1),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: client => {

          this.client.set(client);
          this.error.set(false);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load client details:',
            error
          );

          this.client.set(null);
          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    const email =
      this.clientEmail();

    if (!email) {

      this.loadLoggedInClient();

      return;
    }

    this.loadClient(email);
  }


  // =========================================================
  // HELPERS
  // =========================================================

  getInitials(
    name: string | undefined
  ): string {

    if (!name) {
      return '?';
    }

    return name
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map(
        part => part.charAt(0).toUpperCase()
      )
      .join('');
  }


  formatBudget(
    budget: number | undefined
  ): string {

    if (budget === undefined || budget === null) {
      return '€0.00';
    }

    return new Intl.NumberFormat(
      'en-EU',
      {
        style: 'currency',
        currency: 'EUR'
      }
    ).format(budget);
  }


  getRoleLabel(
    role: string | undefined
  ): string {

    if (!role) {
      return 'Client';
    }

    return role
      .toLowerCase()
      .replace(
        /^./,
        char => char.toUpperCase()
      );
  }
}
