import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  form,
  FormField,
  required,
  minLength,
  maxLength
} from '@angular/forms/signals';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { Subject, finalize, takeUntil } from 'rxjs';
import {SupplierAddresses} from '../../../services/supplier-services/supplier-addresses';
import {CreateSupplierAddress} from '../../models/create-supplier-address.model';
import {AddressStatus} from '../../models/address-status.enum';
import {MatDialogRef} from '@angular/material/dialog';


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

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddSupplierAddress implements OnDestroy {

  private readonly dialogRef =
    inject(MatDialogRef<AddSupplierAddress>);

  private readonly keycloak = inject(Keycloak);

  private readonly supplierAddressesService =
    inject(SupplierAddresses);

  private readonly destroy$ =
    new Subject<void>();


  // ---------------------------------------------------------
  // FORM MODEL
  // ---------------------------------------------------------

  readonly formModel = signal({
    road: '',
    number: null as number | null,
    location: ''
  });


  // ---------------------------------------------------------
  // SIGNAL FORM
  // ---------------------------------------------------------

  readonly addressForm = form(
    this.formModel,
    (path) => {

      required(path.road);
      minLength(path.road, 2);
      maxLength(path.road, 100);

      required(path.number);

      required(path.location);
      minLength(path.location, 2);
      maxLength(path.location, 150);
    }
  );


  // ---------------------------------------------------------
  // STATE
  // ---------------------------------------------------------

  readonly loading =
    signal(false);

  readonly successMessage =
    signal('');

  readonly errorMessage =
    signal('');

  readonly supplierEmail =
    signal('');


  // ---------------------------------------------------------
  // CONSTRUCTOR
  // ---------------------------------------------------------

  constructor() {

    this.loadSupplierEmail();

  }


  // ---------------------------------------------------------
  // KEYCLOAK
  // ---------------------------------------------------------

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

    this.supplierEmail.set(email);
  }


  // ---------------------------------------------------------
  // SUBMIT
  // ---------------------------------------------------------

  submit(): void {

    this.clearMessages();

    /*
     * Prevent submission if the form is invalid.
     */
    if (!this.addressForm().valid()) {

      this.errorMessage.set(
        'Please correct the errors in the form before continuing.'
      );

      return;
    }


    const email =
      this.supplierEmail();

    if (!email) {

      this.errorMessage.set(
        'Your supplier account could not be identified.'
      );

      return;
    }


    const model =
      this.formModel();


    /*
     * IMPORTANT:
     *
     * addressStatus is intentionally NOT included.
     *
     * The backend automatically assigns the status
     * when the address is created.
     */
    const payload: CreateSupplierAddress = {

      road: model.road.trim(),

      number: model.number!,

      location: model.location.trim(),

      supplierEmail: email,

      addressStatus: AddressStatus.AVAILABLE
    };


    this.loading.set(true);


    this.supplierAddressesService
      .addAddress(payload)
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {
          this.loading.set(false);
        })
      )
      .subscribe({

        next: () => {

          this.successMessage.set(
            'The address has been successfully added.'
          );

          this.dialogRef.close(true);

          this.resetForm();
        },


        error: (error) => {

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


  // ---------------------------------------------------------
  // RESET
  // ---------------------------------------------------------

  resetForm(): void {

    this.formModel.set({
      road: '',
      number: null,
      location: ''
    });

  }


  // ---------------------------------------------------------
  // CLEAR MESSAGES
  // ---------------------------------------------------------

  clearMessages(): void {

    this.successMessage.set('');

    this.errorMessage.set('');
  }


  // ---------------------------------------------------------
  // ERROR HANDLING
  // ---------------------------------------------------------

  private getErrorMessage(error: any): string {

    if (error?.error?.message) {
      return error.error.message;
    }

    if (error?.error?.error) {
      return error.error.error;
    }

    if (error?.status === 400) {
      return 'The address information is invalid.';
    }

    if (error?.status === 401) {
      return 'Your session has expired. Please log in again.';
    }

    if (error?.status === 403) {
      return 'You are not allowed to add an address.';
    }

    if (error?.status === 404) {
      return 'The supplier account could not be found.';
    }

    if (error?.status === 409) {
      return 'This address is already assigned.';
    }

    if (error?.status >= 500) {
      return 'A server error occurred. Please try again later.';
    }

    return 'Unable to add the address. Please try again.';
  }


  // ---------------------------------------------------------
  // LIFECYCLE
  // ---------------------------------------------------------

  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();
  }
}
