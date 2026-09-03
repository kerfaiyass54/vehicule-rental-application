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
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatSelectModule
} from '@angular/material/select';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  forkJoin,
  takeUntil
} from 'rxjs';

import {
  RepairDetails as RepairDetailsService
} from '../../services/repair-services/repair-details';

import {
  RepairProfile
} from '../models/repair-profile.model';

import {
  Location
} from '../models/location.model';


@Component({
  selector: 'app-repair-details',

  standalone: true,

  imports: [
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTooltipModule
  ],

  templateUrl: './repair-details.html',

  styleUrl: './repair-details.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class RepairDetails
  implements OnInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly repairService =
    inject(RepairDetailsService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // DESTROY
  // =========================================================

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly profile =
    signal<RepairProfile | null>(null);

  readonly locations =
    signal<Location[]>([]);

  readonly loading =
    signal(true);

  readonly updatingLocation =
    signal(false);

  readonly error =
    signal(false);

  readonly locationError =
    signal(false);

  readonly updateSuccess =
    signal(false);

  readonly repairEmail =
    signal('');

  readonly selectedLocationId =
    signal<number | null>(null);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // GET KEYCLOAK EMAIL
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;


    if (
      !email ||
      !email.trim()
    ) {

      this.error.set(true);

      this.loading.set(false);

      this.cdr.markForCheck();

      return;

    }


    this.repairEmail.set(
      email.trim()
    );

    this.loadData();

  }


  // =========================================================
  // LOAD DETAILS + LOCATIONS
  // =========================================================

  loadData(): void {

    const email =
      this.repairEmail();


    if (!email) {

      return;

    }


    this.loading.set(true);

    this.error.set(false);

    this.locationError.set(false);

    this.updateSuccess.set(false);


    forkJoin({

      profile:
        this.repairService.getInfo(
          email
        ),

      locations:
        this.repairService.getLocations()

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

          this.profile.set(
            response.profile
          );

          this.locations.set(
            response.locations ?? []
          );


          // -----------------------------------------------------
          // SELECT CURRENT LOCATION
          // -----------------------------------------------------

          const currentLocation =
            response.locations.find(
              location =>
                location.name ===
                response.profile.locationName
            );


          if (currentLocation) {

            this.selectedLocationId.set(
              currentLocation.idLoc
            );

          } else {

            this.selectedLocationId.set(
              null
            );

          }


          this.error.set(false);

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to load repair center details:',
            error
          );

          this.profile.set(
            null
          );

          this.locations.set(
            []
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
      this.loading() ||
      this.updatingLocation()
    ) {

      return;

    }


    this.loadData();

  }


  // =========================================================
  // LOCATION SELECTION
  // =========================================================

  onLocationChange(
    locationId: number
  ): void {

    this.selectedLocationId.set(
      Number(locationId)
    );

    this.updateSuccess.set(
      false
    );

    this.locationError.set(
      false
    );

  }


  // =========================================================
  // CHECK LOCATION CHANGE
  // =========================================================

  get locationChanged(): boolean {

    const currentProfile =
      this.profile();

    const selectedId =
      this.selectedLocationId();


    if (
      !currentProfile ||
      !selectedId
    ) {

      return false;

    }


    const currentLocation =
      this.locations().find(
        location =>
          location.name ===
          currentProfile.locationName
      );


    return (
      !currentLocation ||
      currentLocation.idLoc !== selectedId
    );

  }


  // =========================================================
  // SELECTED LOCATION
  // =========================================================

  get selectedLocation(): Location | null {

    const id =
      this.selectedLocationId();


    if (!id) {

      return null;

    }


    return (
      this.locations().find(
        location =>
          location.idLoc === id
      ) ?? null
    );

  }


  // =========================================================
  // UPDATE LOCATION
  // =========================================================

  updateLocation(): void {

    const email =
      this.repairEmail();

    const locationId =
      this.selectedLocationId();


    if (
      !email ||
      !locationId ||
      !this.locationChanged ||
      this.updatingLocation()
    ) {

      return;

    }


    this.updatingLocation.set(
      true
    );

    this.locationError.set(
      false
    );

    this.updateSuccess.set(
      false
    );


    this.repairService

      .updateLocation(
        email,
        locationId
      )

      .pipe(

        takeUntil(
          this.destroy$
        ),

        finalize(() => {

          this.updatingLocation.set(
            false
          );

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: updatedProfile => {

          this.profile.set(
            updatedProfile
          );


          /*
           * Keep the selected location synchronized
           * with the updated profile.
           */

          const newLocation =
            this.locations().find(
              location =>
                location.idLoc ===
                locationId
            );


          if (newLocation) {

            this.selectedLocationId.set(
              newLocation.idLoc
            );

          }


          this.updateSuccess.set(
            true
          );

          this.locationError.set(
            false
          );

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to update repair location:',
            error
          );

          this.locationError.set(
            true
          );

          this.updateSuccess.set(
            false
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    this.loadData();

  }

}
