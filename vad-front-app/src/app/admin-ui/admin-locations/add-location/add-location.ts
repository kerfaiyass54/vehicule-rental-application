import {
  Component,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  LocationManagementService
} from '../../../services/admin-services/location-management.service';


@Component({
  selector: 'app-add-location',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatIconModule
  ],

  templateUrl: './add-location.html',
  styleUrl: './add-location.css'
})
export class AddLocation {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly router =
    inject(Router);

  private readonly locationService =
    inject(LocationManagementService);


  // =========================================================
  // FORM
  // =========================================================

  location = {
    name: '',
    country: '',
    position: ''
  };


  // =========================================================
  // STATE
  // =========================================================

  saving = false;

  submitted = false;

  successMessage = '';

  errorMessage = '';


  // =========================================================
  // CREATE LOCATION
  // =========================================================

  createLocation(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';


    const name =
      this.location.name.trim();

    const country =
      this.location.country.trim();

    const position =
      this.location.position.trim();


    if (
      !name ||
      !country ||
      !position
    ) {

      return;

    }


    if (this.saving) {

      return;

    }


    this.saving = true;


    this.locationService
      .createLocation({

        id: 0,

        name,

        country,

        position

      })
      .subscribe({

        next: () => {

          this.saving = false;

          this.successMessage =
            'Location created successfully.';

          this.location = {

            name: '',
            country: '',
            position: ''

          };

          this.submitted = false;


          setTimeout(() => {

            this.router.navigate([
              '/admin/locations'
            ]);

          }, 800);

        },

        error: error => {

          console.error(
            'Error creating location:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create the location.';

        }

      });

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    if (this.saving) {

      return;

    }

    this.router.navigate([
      '/admin/locations'
    ]);

  }


  // =========================================================
  // VALIDATION
  // =========================================================

  isInvalid(
    value: string
  ): boolean {

    return this.submitted &&
      !value.trim();

  }

}
