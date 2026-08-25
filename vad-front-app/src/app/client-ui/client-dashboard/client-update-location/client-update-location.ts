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
import { ReactiveFormsModule } from '@angular/forms';
import Keycloak from 'keycloak-js';

import { Subject, finalize, takeUntil } from 'rxjs';
import {ClientLocationService} from '../../../services/client-services/client-location.service';




@Component({
  selector: 'app-client-update-location',
  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule
  ],

  templateUrl: './client-update-location.html',
  styleUrl: './client-update-location.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientUpdateLocation implements OnInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly clientLocationService =
    inject(ClientLocationService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly clientEmail =
    signal('');

  readonly location =
    signal<any | null>(null);

  readonly loading =
    signal(true);

  readonly updating =
    signal(false);

  readonly error =
    signal('');

  readonly success =
    signal('');


  // =========================================================
  // FORM STATE
  // =========================================================

  readonly name =
    signal('');

  readonly country =
    signal('');

  readonly position =
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
  // KEYCLOAK
  // =========================================================

  private loadClientEmail(): void {

    const token = this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;

    if (!email) {

      this.error.set(
        'Unable to retrieve your email from Keycloak.'
      );

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
    this.error.set('');
    this.success.set('');

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

        next: location => {

          this.location.set(location);

          this.name.set(
            location?.name ?? ''
          );

          this.country.set(
            location?.country ?? ''
          );

          this.position.set(
            location?.position ?? ''
          );

          this.error.set('');

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load client location',
            error
          );

          this.location.set(null);

          this.error.set(
            'Unable to load your current location.'
          );

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // INPUTS
  // =========================================================

  onNameChange(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    this.name.set(input.value);

    this.clearMessages();
  }


  onCountryChange(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    this.country.set(input.value);

    this.clearMessages();
  }


  onPositionChange(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    this.position.set(input.value);

    this.clearMessages();
  }


  // =========================================================
  // UPDATE
  // =========================================================

  updateLocation(): void {

    this.error.set('');
    this.success.set('');

    const name =
      this.name().trim();

    const country =
      this.country().trim();

    const position =
      this.position().trim();


    // -------------------------------------------------------
    // VALIDATION
    // -------------------------------------------------------

    if (!name) {

      this.error.set(
        'any name is required.'
      );

      return;
    }

    if (!country) {

      this.error.set(
        'Country is required.'
      );

      return;
    }

    if (!position) {

      this.error.set(
        'Position is required.'
      );

      return;
    }


    const currentLocation =
      this.location();


    const location: any = {

      idLoc:
        currentLocation?.idLoc ?? 0,

      name,

      country,

      position
    };


    this.updating.set(true);

    this.clientLocationService
      .updateLocation(
        this.clientEmail(),
        location
      )
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.updating.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: updatedLocation => {

          this.location.set(
            updatedLocation
          );

          this.name.set(
            updatedLocation.name
          );

          this.country.set(
            updatedLocation.country
          );

          this.position.set(
            updatedLocation.position
          );

          this.success.set(
            'Your location has been updated successfully.'
          );

          this.error.set('');

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to update client location',
            error
          );

          this.error.set(
            'Unable to update your location. Please try again.'
          );

          this.success.set('');

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // RESET
  // =========================================================

  resetForm(): void {

    const currentLocation =
      this.location();

    this.name.set(
      currentLocation?.name ?? ''
    );

    this.country.set(
      currentLocation?.country ?? ''
    );

    this.position.set(
      currentLocation?.position ?? ''
    );

    this.clearMessages();
  }


  // =========================================================
  // HELPERS
  // =========================================================

  private clearMessages(): void {

    if (this.error()) {
      this.error.set('');
    }

    if (this.success()) {
      this.success.set('');
    }
  }
}
