import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  form,
  FormField,
  required,
  minLength,
  maxLength
} from '@angular/forms/signals';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

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
  MatDialogRef
} from '@angular/material/dialog';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  SupplierAddresses
} from '../../../services/supplier-services/supplier-addresses';

import {
  UserLocationValidationService
} from '../../../services/user-location-validation.service';

import {
  CreateSupplierAddress
} from '../../models/create-supplier-address.model';

import {
  AddressStatus
} from '../../models/address-status.enum';

import {
  LocationValidation
} from '../../../models/location-validation.model';


@Component({
  selector: 'app-add-supplier-address',
  standalone: true,

  imports: [
    CommonModule,
    FormField,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],

  templateUrl: './add-supplier-address.html',
  styleUrl: './add-supplier-address.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class AddSupplierAddress
  implements OnInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly dialogRef =
    inject(MatDialogRef<AddSupplierAddress>);

  private readonly keycloak =
    inject(Keycloak);

  private readonly supplierAddressesService =
    inject(SupplierAddresses);

  private readonly validationService =
    inject(UserLocationValidationService);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // FORM MODEL
  // =========================================================

  readonly formModel =
    signal({

      road:
        '',

      number:
        null as number | null,

      location:
        ''

    });


  // =========================================================
  // SIGNAL FORM
  // =========================================================

  readonly addressForm =
    form(
      this.formModel,

      (path) => {

        // -------------------------------------------------------
        // ROAD
        // -------------------------------------------------------

        required(path.road);

        minLength(
          path.road,
          2
        );

        maxLength(
          path.road,
          100
        );


        // -------------------------------------------------------
        // NUMBER
        // -------------------------------------------------------

        required(path.number);


        // -------------------------------------------------------
        // LOCATION
        // -------------------------------------------------------

        required(path.location);

        minLength(
          path.location,
          2
        );

        maxLength(
          path.location,
          150
        );

      }
    );


  // =========================================================
  // LOCATIONS
  // =========================================================

  readonly locations =
    signal<LocationValidation[]>([]);


  // =========================================================
  // STATE
  // =========================================================

  readonly loading =
    signal(false);

  readonly loadingLocations =
    signal(false);

  readonly locationsError =
    signal(false);

  readonly successMessage =
    signal('');

  readonly errorMessage =
    signal('');

  readonly supplierEmail =
    signal('');


  // =========================================================
  // INITIALIZATION
  // =========================================================

  ngOnInit(): void {

    this.loadSupplierEmail();

    this.loadLocations();

  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadSupplierEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';


    if (!email) {

      this.errorMessage.set(
        'Unable to retrieve your email from Keycloak.'
      );

      return;
    }


    this.supplierEmail.set(
      email
    );

  }


  // =========================================================
  // LOAD LOCATIONS
  // =========================================================

  loadLocations(): void {

    this.loadingLocations.set(
      true
    );

    this.locationsError.set(
      false
    );


    this.validationService
      .getAllLocations()
      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loadingLocations.set(
            false
          );

        })

      )
      .subscribe({

        next: locations => {

          this.locations.set(
            (locations ?? [])
              .filter(
                location =>
                  !!location?.name?.trim()
              )
              .sort(
                (a, b) =>
                  a.name.localeCompare(
                    b.name
                  )
              )
          );


          /*
           * Keep the previously selected value
           * only if it still exists.
           */
          const currentLocation =
            this.formModel().location;


          if (
            currentLocation &&
            !this.locations().some(
              location =>
                location.name === currentLocation
            )
          ) {

            this.formModel.update(
              model => ({
                ...model,
                location: ''
              })
            );

          }

        },


        error: error => {

          console.error(
            'Unable to load locations:',
            error
          );


          this.locations.set([]);


          this.locationsError.set(
            true
          );


          this.errorMessage.set(
            'Unable to load available locations. Please try again.'
          );

        }

      });

  }


  // =========================================================
  // SUBMIT
  // =========================================================

  submit(): void {

    this.clearMessages();


    // -------------------------------------------------------
    // FORM VALIDATION
    // -------------------------------------------------------

    if (
      !this.addressForm().valid()
    ) {

      this.errorMessage.set(
        'Please correct the errors in the form before continuing.'
      );

      return;

    }


    // -------------------------------------------------------
    // SUPPLIER EMAIL
    // -------------------------------------------------------

    const email =
      this.supplierEmail();


    if (!email) {

      this.errorMessage.set(
        'Your supplier account could not be identified.'
      );

      return;

    }


    // -------------------------------------------------------
    // LOCATION VALIDATION
    // -------------------------------------------------------

    const model =
      this.formModel();

    const selectedLocation =
      this.locations().find(
        location =>
          location.name === model.location
      );


    if (!selectedLocation) {

      this.errorMessage.set(
        'Please select a valid registered location.'
      );

      return;

    }


    // -------------------------------------------------------
    // PREVENT DOUBLE SUBMISSION
    // -------------------------------------------------------

    if (this.loading()) {
      return;
    }


    // -------------------------------------------------------
    // REQUEST PAYLOAD
    // -------------------------------------------------------

    /*
     * addressStatus is kept here exactly as in the
     * existing implementation.
     *
     * The backend can still override it according
     * to its own creation logic.
     */

    const payload:
      CreateSupplierAddress = {

      road:
        model.road.trim(),

      number:
        model.number!,

      location:
        selectedLocation.name.trim(),

      supplierEmail:
      email,

      addressStatus:
      AddressStatus.AVAILABLE

    };


    // -------------------------------------------------------
    // SEND REQUEST
    // -------------------------------------------------------

    this.loading.set(
      true
    );


    this.supplierAddressesService
      .addAddress(payload)

      .pipe(

        takeUntil(
          this.destroy$
        ),

        finalize(() => {

          this.loading.set(
            false
          );

        })

      )

      .subscribe({

        next: () => {

          this.successMessage.set(
            'The address has been successfully added.'
          );


          this.dialogRef.close(
            true
          );


          this.resetForm();

        },


        error: error => {

          console.error(
            'Unable to add supplier address',
            error
          );


          this.errorMessage.set(
            this.getErrorMessage(error)
          );

        }

      });

  }


  // =========================================================
  // RESET
  // =========================================================

  resetForm(): void {

    this.formModel.set({

      road:
        '',

      number:
        null,

      location:
        ''

    });


    this.clearMessages();

  }


  // =========================================================
  // CLEAR MESSAGES
  // =========================================================

  clearMessages(): void {

    this.successMessage.set(
      ''
    );

    this.errorMessage.set(
      ''
    );

  }


  // =========================================================
  // ERROR HANDLING
  // =========================================================

  private getErrorMessage(
    error: any
  ): string {

    if (
      error?.error?.message
    ) {

      return error.error.message;

    }


    if (
      error?.error?.error
    ) {

      return error.error.error;

    }


    if (
      error?.status === 400
    ) {

      return (
        'The address information is invalid.'
      );

    }


    if (
      error?.status === 401
    ) {

      return (
        'Your session has expired. Please log in again.'
      );

    }


    if (
      error?.status === 403
    ) {

      return (
        'You are not allowed to add an address.'
      );

    }


    if (
      error?.status === 404
    ) {

      return (
        'The supplier account could not be found.'
      );

    }


    if (
      error?.status === 409
    ) {

      return (
        'This address is already assigned.'
      );

    }


    if (
      error?.status >= 500
    ) {

      return (
        'A server error occurred. Please try again later.'
      );

    }


    return (
      'Unable to add the address. Please try again.'
    );

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }

}
