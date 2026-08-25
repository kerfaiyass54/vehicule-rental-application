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

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { Subject, finalize, takeUntil } from 'rxjs';
import {ClientLocationService} from '../../../services/client-services/client-location.service';
import {ClientUpdateLocation} from '../client-update-location/client-update-location';



@Component({
  selector: 'app-client-location',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatDialogModule
  ],

  templateUrl: './client-location.html',
  styleUrl: './client-location.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientLocation implements OnInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly clientLocationService =
    inject(ClientLocationService);

  private readonly dialog =
    inject(MatDialog);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly location =
    signal<any | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly updating =
    signal(false);

  readonly clientEmail =
    signal('');


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadClientEmail();
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  // =========================================================
  // LOAD CLIENT EMAIL
  // =========================================================

  private loadClientEmail(): void {

    const token = this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;

    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.clientEmail.set(email);

    this.loadLocation();
  }


  // =========================================================
  // LOAD LOCATION
  // =========================================================

  loadLocation(): void {

    const email = this.clientEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.clientLocationService
      .getLocation(email)
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: response => {

          this.location.set(response);
          this.error.set(false);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load client location',
            error
          );

          this.location.set(null);
          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // UPDATE LOCATION DIALOG
  // =========================================================

  openUpdateLocationDialog(): void {

    const currentLocation =
      this.location();

    const dialogRef =
      this.dialog.open(ClientUpdateLocation, {

        width: '680px',

        maxWidth: '95vw',

        maxHeight: '90vh',

        autoFocus: false,

        panelClass: 'update-location-dialog',

        data: {
          location: currentLocation
        }
      });


    dialogRef.afterClosed()
      .pipe(
        takeUntil(this.destroy$)
      )
      .subscribe((updated: boolean) => {

        if (!updated) {
          return;
        }

        this.loadLocation();

      });
  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadLocation();
  }


  // =========================================================
  // HELPERS
  // =========================================================

  getLocationIcon(): string {

    const currentLocation =
      this.location();

    if (!currentLocation) {
      return 'location_off';
    }

    return 'location_on';
  }


  getLocationDescription(): string {

    const currentLocation =
      this.location();

    if (!currentLocation) {
      return 'No location information available';
    }

    return `${currentLocation.name}, ${currentLocation.country}`;
  }
}
