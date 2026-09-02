import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  form,
  required,
  minLength,
  maxLength,
  min,
  max
} from '@angular/forms/signals';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatSelectModule
} from '@angular/material/select';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  Subject,
  EMPTY,
  finalize,
  takeUntil,
  switchMap
} from 'rxjs';

import Keycloak from 'keycloak-js';

import {
  SupplierVehicule
} from '../../../services/supplier-services/supplier-vehicule';

import {
  CreateVehicule
} from '../../models/create-vehicule.model';

import {
  Transmission
} from '../../models/transmission.enum';


@Component({
  selector: 'app-add-vehicule',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,

    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './add-vehicule.html',
  styleUrl: './add-vehicule.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class AddVehicule
  implements OnInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly supplierVehiculeService =
    inject(SupplierVehicule);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // TRANSMISSION
  // =========================================================

  readonly transmissions = [

    {
      value:
      Transmission.MANUAL,

      label:
        'Manual',

      icon:
        'settings'
    },

    {
      value:
      Transmission.AUTOMATIC,

      label:
        'Automatic',

      icon:
        'auto_mode'
    }

  ];


  // =========================================================
  // FORM MODEL
  // =========================================================

  readonly vehicleModel =
    signal<CreateVehicule>({

      nameVehicule:
        '',

      color:
        '',

      brand:
        '',

      price:
        0,

      highSpeed:
        0,

      transmission:
      Transmission.MANUAL

    });


  // =========================================================
  // SIGNAL FORM
  // =========================================================

  readonly vehicleForm =
    form(
      this.vehicleModel,
      (schema) => {

        required(
          schema.nameVehicule
        );

        minLength(
          schema.nameVehicule,
          2
        );

        maxLength(
          schema.nameVehicule,
          100
        );


        required(
          schema.color
        );

        minLength(
          schema.color,
          2
        );

        maxLength(
          schema.color,
          50
        );


        required(
          schema.brand
        );

        minLength(
          schema.brand,
          2
        );

        maxLength(
          schema.brand,
          50
        );


        required(
          schema.price
        );

        min(
          schema.price,
          1
        );

        max(
          schema.price,
          10000000
        );


        required(
          schema.highSpeed
        );

        min(
          schema.highSpeed,
          1
        );

        max(
          schema.highSpeed,
          500
        );


        required(
          schema.transmission
        );

      }
    );


  // =========================================================
  // VEHICLE NAMES
  // =========================================================

  readonly vehicleNames =
    signal<string[]>([]);


  readonly loadingNames =
    signal(false);


  readonly nameAlreadyExists =
    signal(false);


  // =========================================================
  // STATE
  // =========================================================

  readonly submitting =
    signal(false);

  readonly success =
    signal(false);

  readonly error =
    signal(false);

  readonly errorMessage =
    signal('');

  readonly supplierEmail =
    signal('');

  readonly submitted =
    signal(false);


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadSupplierEmail();

  }


  // =========================================================
  // KEYCLOAK EMAIL
  // =========================================================

  private loadSupplierEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';


    if (!email) {

      this.error.set(true);

      this.errorMessage.set(
        'Unable to determine the supplier account from Keycloak.'
      );

      this.cdr.markForCheck();

      return;
    }


    this.supplierEmail.set(
      email
    );


    this.loadVehicleNames();

  }


  // =========================================================
  // LOAD VEHICLE NAMES
  // =========================================================

  loadVehicleNames(): void {

    const email =
      this.supplierEmail();


    if (!email) {
      return;
    }


    this.loadingNames.set(
      true
    );


    this.supplierVehiculeService
      .getVehicleNames(email)

      .pipe(

        takeUntil(
          this.destroy$
        ),

        finalize(() => {

          this.loadingNames.set(
            false
          );

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: names => {

          this.vehicleNames.set(
            (names ?? [])
              .filter(
                name =>
                  !!name?.trim()
              )
          );


          this.updateNameUniqueness(
            this.vehicleModel()
              .nameVehicule
          );

        },


        error: error => {

          console.error(
            'Unable to load vehicle names',
            error
          );


          this.vehicleNames.set(
            []
          );

          /*
           * We do not block the form here.
           * The name list will be refreshed again
           * immediately before creation.
           */

        }

      });

  }


  // =========================================================
  // VEHICLE NAME CHANGE
  // =========================================================

  updateVehicleName(
    value: string
  ): void {

    this.vehicleModel.update(
      vehicle => ({
        ...vehicle,
        nameVehicule:
        value
      })
    );


    this.updateNameUniqueness(
      value
    );


    this.error.set(false);

    this.errorMessage.set('');

  }


  // =========================================================
  // CHECK NAME UNIQUENESS
  // =========================================================

  private updateNameUniqueness(
    name: string
  ): void {

    const normalizedName =
      this.normalizeName(name);


    if (!normalizedName) {

      this.nameAlreadyExists.set(
        false
      );

      return;
    }


    const exists =
      this.vehicleNames().some(
        existingName =>
          this.normalizeName(
            existingName
          ) === normalizedName
      );


    this.nameAlreadyExists.set(
      exists
    );

  }


  // =========================================================
  // NORMALIZE VEHICLE NAME
  // =========================================================

  private normalizeName(
    name: string
  ): string {

    return name
      .trim()
      .replace(
        /\s+/g,
        ' '
      )
      .toLowerCase();

  }


  // =========================================================
  // SUBMIT
  // =========================================================

  submit(): void {

    this.submitted.set(
      true
    );

    this.error.set(
      false
    );

    this.errorMessage.set(
      ''
    );

    this.success.set(
      false
    );


    // -------------------------------------------------------
    // FORM VALIDATION
    // -------------------------------------------------------

    if (
      this.vehicleForm().invalid()
    ) {

      this.cdr.markForCheck();

      return;

    }


    // -------------------------------------------------------
    // KEYCLOAK EMAIL
    // -------------------------------------------------------

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';


    if (!email) {

      this.error.set(
        true
      );

      this.errorMessage.set(
        'Unable to determine the supplier account from Keycloak.'
      );

      this.cdr.markForCheck();

      return;

    }


    this.supplierEmail.set(
      email
    );


    // -------------------------------------------------------
    // VEHICLE DATA
    // -------------------------------------------------------

    const model =
      this.vehicleModel();


    const vehicle:
      CreateVehicule = {

      nameVehicule:
        model.nameVehicule.trim(),

      color:
        model.color.trim(),

      brand:
        model.brand.trim(),

      price:
        Number(
          model.price
        ),

      highSpeed:
        Number(
          model.highSpeed
        ),

      transmission:
      model.transmission

    };


    // -------------------------------------------------------
    // PREVENT DOUBLE SUBMISSION
    // -------------------------------------------------------

    if (
      this.submitting()
    ) {

      return;

    }


    // -------------------------------------------------------
    // SUBMIT
    // -------------------------------------------------------

    this.submitting.set(
      true
    );

    this.cdr.markForCheck();


    /*
     * Fetch the current names one more time immediately
     * before creation.
     *
     * This avoids relying only on the names loaded when
     * the component was initialized.
     */

    this.supplierVehiculeService
      .getVehicleNames(email)

      .pipe(

        takeUntil(
          this.destroy$
        ),

        switchMap(
          names => {

            const currentNames =
              names ?? [];


            this.vehicleNames.set(
              currentNames
            );


            const normalizedName =
              this.normalizeName(
                vehicle.nameVehicule
              );


            const duplicate =
              currentNames.some(
                existingName =>
                  this.normalizeName(
                    existingName
                  ) === normalizedName
              );


            if (duplicate) {

              this.nameAlreadyExists.set(
                true
              );

              this.error.set(
                true
              );

              this.errorMessage.set(
                `The vehicle name "${vehicle.nameVehicule}" is already used by your fleet. Please choose another name.`
              );

              this.cdr.markForCheck();


              return EMPTY;

            }


            this.nameAlreadyExists.set(
              false
            );


            return this.supplierVehiculeService
              .addVehicle(
                email,
                vehicle
              );

          }
        ),

        finalize(() => {

          this.submitting.set(
            false
          );

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.success.set(
            true
          );

          this.error.set(
            false
          );

          this.errorMessage.set(
            ''
          );


          // ---------------------------------------------------
          // RESET
          // ---------------------------------------------------

          this.vehicleModel.set({

            nameVehicule:
              '',

            color:
              '',

            brand:
              '',

            price:
              0,

            highSpeed:
              0,

            transmission:
            Transmission.MANUAL

          });


          this.vehicleNames.update(
            names =>
              names.concat(
                vehicle.nameVehicule
              )
          );


          this.nameAlreadyExists.set(
            false
          );

          this.submitted.set(
            false
          );


          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to create vehicle',
            error
          );


          this.success.set(
            false
          );

          this.error.set(
            true
          );


          if (
            error?.status === 400
          ) {

            this.errorMessage.set(
              'The vehicle information is invalid. Please check the entered values.'
            );

          } else if (
            error?.status === 401
          ) {

            this.errorMessage.set(
              'Your session has expired. Please log in again.'
            );

          } else if (
            error?.status === 403
          ) {

            this.errorMessage.set(
              'You are not allowed to create a vehicle.'
            );

          } else if (
            error?.status === 409
          ) {

            this.nameAlreadyExists.set(
              true
            );

            this.errorMessage.set(
              'A vehicle with this name already exists in your fleet.'
            );

          } else {

            this.errorMessage.set(
              'Unable to create the vehicle. Please try again.'
            );

          }


          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // RESET
  // =========================================================

  resetForm(): void {

    if (
      this.submitting()
    ) {

      return;

    }


    this.vehicleModel.set({

      nameVehicule:
        '',

      color:
        '',

      brand:
        '',

      price:
        0,

      highSpeed:
        0,

      transmission:
      Transmission.MANUAL

    });


    this.submitted.set(
      false
    );

    this.success.set(
      false
    );

    this.error.set(
      false
    );

    this.errorMessage.set(
      ''
    );

    this.nameAlreadyExists.set(
      false
    );


    this.cdr.markForCheck();

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }

}
