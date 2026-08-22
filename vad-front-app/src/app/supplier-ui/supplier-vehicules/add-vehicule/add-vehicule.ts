
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  form,
  required,
  minLength,
  maxLength,
  min,
  max,
  pattern
} from '@angular/forms/signals';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import { Subject, finalize, takeUntil } from 'rxjs';

import Keycloak from 'keycloak-js';

import { SupplierVehicule } from '../../../services/supplier-services/supplier-vehicule';

import { CreateVehicule } from '../../models/create-vehicule.model';
import { Transmission } from '../../models/transmission.enum';


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

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddVehicule {

  private readonly keycloak =
    inject(Keycloak);

  private readonly supplierVehiculeService =
    inject(SupplierVehicule);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // ---------------------------------------------------------
  // TRANSMISSION
  // ---------------------------------------------------------

  readonly transmissions = [
    {
      value: Transmission.MANUAL,
      label: 'Manual',
      icon: 'settings'
    },
    {
      value: Transmission.AUTOMATIC,
      label: 'Automatic',
      icon: 'auto_mode'
    }
  ];


  // ---------------------------------------------------------
  // FORM MODEL
  // ---------------------------------------------------------

  readonly vehicleModel = signal<CreateVehicule>({
    nameVehicule: '',
    color: '',
    brand: '',
    price: 0,
    highSpeed: 0,
    transmission: Transmission.MANUAL
  });


  // ---------------------------------------------------------
  // SIGNAL FORM
  // ---------------------------------------------------------

  readonly vehicleForm = form(this.vehicleModel, (schema) => {

    required(schema.nameVehicule);
    minLength(schema.nameVehicule, 2);
    maxLength(schema.nameVehicule, 100);

    required(schema.color);
    minLength(schema.color, 2);
    maxLength(schema.color, 50);

    required(schema.brand);
    minLength(schema.brand, 2);
    maxLength(schema.brand, 50);

    required(schema.price);
    min(schema.price, 1);
    max(schema.price, 10000000);

    required(schema.highSpeed);
    min(schema.highSpeed, 1);
    max(schema.highSpeed, 500);

    required(schema.transmission);
  });


  // ---------------------------------------------------------
  // STATE
  // ---------------------------------------------------------

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


  // ---------------------------------------------------------
  // SUBMIT
  // ---------------------------------------------------------

  submit(): void {

    this.submitted.set(true);

    this.error.set(false);
    this.errorMessage.set('');
    this.success.set(false);


    /*
     * Signal Forms validation.
     *
     * We don't send anything to the backend
     * if the form is invalid.
     */
    if (this.vehicleForm().invalid()) {

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

      this.error.set(true);

      this.errorMessage.set(
        'Unable to determine the supplier account from Keycloak.'
      );

      this.cdr.markForCheck();

      return;
    }


    this.supplierEmail.set(email);


    // -------------------------------------------------------
    // REQUEST
    // -------------------------------------------------------

    const vehicle: CreateVehicule = {
      nameVehicule:
        this.vehicleModel().nameVehicule.trim(),

      color:
        this.vehicleModel().color.trim(),

      brand:
        this.vehicleModel().brand.trim(),

      price:
        Number(this.vehicleModel().price),

      highSpeed:
        Number(this.vehicleModel().highSpeed),

      transmission:
        this.vehicleModel().transmission
    };


    this.submitting.set(true);

    this.cdr.markForCheck();


    this.supplierVehiculeService
      .addVehicle(
        email,
        vehicle
      )
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.submitting.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: () => {

          this.success.set(true);

          this.error.set(false);

          /*
           * Reset the form after successful creation.
           */
          this.vehicleModel.set({
            nameVehicule: '',
            color: '',
            brand: '',
            price: 0,
            highSpeed: 0,
            transmission: Transmission.MANUAL
          });

          this.submitted.set(false);

          this.cdr.markForCheck();
        },


        error: error => {

          console.error(
            'Unable to create vehicle',
            error
          );

          this.success.set(false);
          this.error.set(true);


          if (error?.status === 400) {

            this.errorMessage.set(
              'The vehicle information is invalid. Please check the entered values.'
            );

          } else if (error?.status === 401) {

            this.errorMessage.set(
              'Your session has expired. Please log in again.'
            );

          } else if (error?.status === 403) {

            this.errorMessage.set(
              'You are not allowed to create a vehicle.'
            );

          } else if (error?.status === 409) {

            this.errorMessage.set(
              'A vehicle with this information already exists.'
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


  // ---------------------------------------------------------
  // RESET
  // ---------------------------------------------------------

  resetForm(): void {

    if (this.submitting()) {
      return;
    }

    this.vehicleModel.set({
      nameVehicule: '',
      color: '',
      brand: '',
      price: 0,
      highSpeed: 0,
      transmission: Transmission.MANUAL
    });

    this.submitted.set(false);
    this.success.set(false);
    this.error.set(false);
    this.errorMessage.set('');

    this.cdr.markForCheck();
  }


  // ---------------------------------------------------------
  // DESTROY
  // ---------------------------------------------------------

  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();
  }
}





