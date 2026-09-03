import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  Subject,
  forkJoin,
  finalize,
  takeUntil
} from 'rxjs';

import {
  ClientService
} from '../../services/client-services/client.service';

import {
  ClientLocationService
} from '../../services/client-services/client-location.service';

import {
  Client
} from '../models/client.model';


/* =========================================================
   LOCATION MODEL
   ========================================================= */

interface ClientLocation {

  idLoc?: number;

  name?: string;

  country?: string;

  position?: string;

}


/* =========================================================
   COMPONENT
   ========================================================= */

@Component({
  selector: 'app-client-info',
  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],

  templateUrl: './client-info.html',
  styleUrl: './client-info.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class ClientInfo
  implements OnInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly clientService =
    inject(ClientService);

  private readonly clientLocationService =
    inject(ClientLocationService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly client =
    signal<Client | null>(null);

  readonly location =
    signal<ClientLocation | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly email =
    signal('');


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadClient();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD CLIENT
  // =========================================================

  private loadClient(): void {

    const token =
      this.keycloak.tokenParsed;

    const clientEmail =
      token?.['email'] as string | undefined;


    if (
      !clientEmail ||
      !clientEmail.trim()
    ) {

      this.loading.set(false);

      this.error.set(true);

      this.cdr.markForCheck();

      return;

    }


    const normalizedEmail =
      clientEmail.trim();


    this.email.set(
      normalizedEmail
    );

    this.loading.set(true);

    this.error.set(false);


    /*
     * Load both sources together:
     *
     * 1. ClientService
     *    → personal/account information
     *
     * 2. ClientLocationService
     *    → current location information
     */

    forkJoin({

      client:
        this.clientService.getClient(
          normalizedEmail
        ),

      location:
        this.clientLocationService.getLocation(
          normalizedEmail
        )

    })
      .pipe(

        takeUntil(
          this.destroy$
        ),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )
      .subscribe({

        next: response => {

          this.client.set(
            response.client
          );

          this.location.set(
            response.location
          );

          this.error.set(false);

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load client information:',
            error
          );

          this.client.set(
            null
          );

          this.location.set(
            null
          );

          this.error.set(
            true
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (
      this.loading()
    ) {

      return;

    }

    this.loadClient();

  }


  // =========================================================
  // INITIALS
  // =========================================================

  getInitials(
    name: string | undefined
  ): string {

    if (
      !name ||
      !name.trim()
    ) {

      return '?';

    }


    const parts =
      name
        .trim()
        .split(/\s+/)
        .filter(Boolean);


    if (
      parts.length === 1
    ) {

      return parts[0]
        .substring(0, 2)
        .toUpperCase();

    }


    return (
      (parts[0]?.charAt(0) ?? '') +
      (parts[parts.length - 1]?.charAt(0) ?? '')
    ).toUpperCase();

  }

}
