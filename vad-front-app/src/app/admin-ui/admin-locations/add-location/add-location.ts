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

import {
  UserLocationValidationService
} from '../../../services/user-location-validation.service';
import {COUNTRIES} from '../../constants/countries';




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

  private readonly validationService =
    inject(UserLocationValidationService);


  // =========================================================
  // COUNTRIES
  // =========================================================

  readonly countries =
    COUNTRIES;


  // =========================================================
  // FORM
  // =========================================================

  location = {

    name: '',

    country: '',

    position: ''

  };


  // =========================================================
  // COUNTRY SEARCH
  // =========================================================

  countrySearch = '';

  countryDropdownOpen = false;


  // =========================================================
  // STATE
  // =========================================================

  saving = false;

  submitted = false;

  checkingLocation = false;

  locationAlreadyExists = false;

  successMessage = '';

  errorMessage = '';

  locationExistsMessage = '';


  // =========================================================
  // FILTERED COUNTRIES
  // =========================================================

  get filteredCountries(): string[] {

    const search =
      this.countrySearch
        .trim()
        .toLowerCase();

    if (!search) {

      return this.countries;

    }

    return this.countries.filter(
      country =>
        country
          .toLowerCase()
          .includes(search)
    );

  }


  // =========================================================
  // OPEN COUNTRY DROPDOWN
  // =========================================================

  openCountryDropdown(): void {

    if (this.saving) {

      return;

    }

    this.countryDropdownOpen = true;

  }


  // =========================================================
  // CLOSE COUNTRY DROPDOWN
  // =========================================================

  closeCountryDropdown(): void {

    /*
     * Small delay allows the click on an option
     * to be processed before closing the dropdown.
     */

    setTimeout(() => {

      this.countryDropdownOpen = false;

    }, 150);

  }


  // =========================================================
  // SELECT COUNTRY
  // =========================================================

  selectCountry(
    country: string
  ): void {

    this.location.country =
      country;

    this.countrySearch =
      country;

    this.countryDropdownOpen =
      false;

    this.locationAlreadyExists =
      false;

    this.locationExistsMessage =
      '';

    this.checkLocation();

  }


  // =========================================================
  // CREATE LOCATION
  // =========================================================

  createLocation(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';

    this.locationExistsMessage = '';

    this.locationAlreadyExists = false;


    // =======================================================
    // CLEAN VALUES
    // =======================================================

    const name =
      this.location.name.trim();

    const country =
      this.location.country.trim();

    const position =
      this.location.position.trim();


    // =======================================================
    // REQUIRED FIELDS
    // =======================================================

    if (
      !name ||
      !country ||
      !position
    ) {

      return;

    }


    // =======================================================
    // PREVENT DOUBLE SUBMISSION
    // =======================================================

    if (
      this.saving ||
      this.checkingLocation
    ) {

      return;

    }


    this.checkingLocation = true;


    // =======================================================
    // CHECK NAME + COUNTRY UNIQUENESS
    // =======================================================

    this.validationService
      .locationExists(
        name,
        country
      )
      .subscribe({

        next: exists => {

          this.checkingLocation = false;


          if (exists) {

            this.locationAlreadyExists = true;

            this.locationExistsMessage =
              'This location already exists for this country.';

            return;

          }


          this.saveLocation(
            name,
            country,
            position
          );

        },

        error: error => {

          console.error(
            'Error checking location uniqueness:',
            error
          );

          this.checkingLocation = false;

          this.errorMessage =
            'Unable to verify whether this location already exists.';

        }

      });

  }


  // =========================================================
  // SAVE LOCATION
  // =========================================================

  private saveLocation(

    name: string,

    country: string,

    position: string

  ): void {

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
  // CHECK LOCATION
  // =========================================================

  checkLocation(): void {

    const name =
      this.location.name.trim();

    const country =
      this.location.country.trim();


    this.locationAlreadyExists = false;

    this.locationExistsMessage = '';


    if (
      !name ||
      !country
    ) {

      return;

    }


    this.validationService
      .locationExists(
        name,
        country
      )
      .subscribe({

        next: exists => {

          this.locationAlreadyExists =
            exists;


          if (exists) {

            this.locationExistsMessage =
              'This location already exists for this country.';

          }

        },

        error: error => {

          console.error(
            'Error checking location:',
            error
          );

        }

      });

  }


  // =========================================================
  // FIELD VALIDATION
  // =========================================================

  isInvalid(
    value: string
  ): boolean {

    return this.submitted &&
      !value.trim();

  }


  // =========================================================
  // NAME INVALID
  // =========================================================

  isNameInvalid(): boolean {

    return (
      this.isInvalid(
        this.location.name
      ) ||
      this.locationAlreadyExists
    );

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    if (
      this.saving ||
      this.checkingLocation
    ) {

      return;

    }


    this.router.navigate([
      '/admin/creation'
    ]);

  }

}
