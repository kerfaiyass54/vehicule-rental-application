import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
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
  ClientManagementService
} from '../../../services/admin-services/client-management.service';

import {
  UserLocationValidationService
} from '../../../services/user-location-validation.service';

import {
  LocationValidation
} from '../../../models/location-validation.model';
import {NATIONALITIES} from '../../constants/nationalities';




@Component({
  selector: 'app-add-client',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatIconModule
  ],

  templateUrl: './add-client.html',
  styleUrl: './add-client.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddClient implements OnInit {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly router =
    inject(Router);

  private readonly clientService =
    inject(ClientManagementService);

  private readonly validationService =
    inject(UserLocationValidationService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // FORM
  // =========================================================

  client = {

    nameClient: '',

    email: '',

    nationality: '',

    budget: 0,

    locationId: null as number | null,

    locationName: '',

    locationCountry: ''

  };


  // =========================================================
  // LOCATIONS
  // =========================================================

  locations: LocationValidation[] = [];


  // =========================================================
  // NATIONALITIES
  // =========================================================

  readonly nationalities =
    NATIONALITIES;

  nationalitySearch = '';

  nationalityDropdownOpen = false;


  // =========================================================
  // STATE
  // =========================================================

  submitted = false;

  loadingLocations = false;

  saving = false;

  checkingName = false;

  checkingEmail = false;

  nameAlreadyExists = false;

  emailAlreadyExists = false;

  successMessage = '';

  errorMessage = '';


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadLocations();

  }


  // =========================================================
  // LOAD LOCATIONS
  // =========================================================

  private loadLocations(): void {

    this.loadingLocations = true;

    this.validationService
      .getAllLocations()
      .subscribe({

        next: locations => {

          this.locations =
            locations ?? [];

          this.loadingLocations = false;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load locations:',
            error
          );

          this.locations = [];

          this.loadingLocations = false;

          this.errorMessage =
            'Unable to load locations.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // LOCATION CHANGE
  // =========================================================

  onLocationChange(): void {

    if (
      this.client.locationId === null
    ) {

      this.client.locationName = '';

      this.client.locationCountry = '';

      return;

    }


    const location =
      this.locations.find(
        item =>
          item.id ===
          this.client.locationId
      );


    if (!location) {

      this.client.locationName = '';

      this.client.locationCountry = '';

      return;

    }


    this.client.locationName =
      location.name;

    this.client.locationCountry =
      location.country;

  }


  // =========================================================
  // NATIONALITY SEARCH
  // =========================================================

  get filteredNationalities(): string[] {

    const search =
      this.nationalitySearch
        .trim()
        .toLowerCase();


    if (!search) {

      return this.nationalities;

    }


    return this.nationalities.filter(
      nationality =>
        nationality
          .toLowerCase()
          .includes(search)
    );

  }


  // =========================================================
  // OPEN NATIONALITY DROPDOWN
  // =========================================================

  openNationalityDropdown(): void {

    this.nationalityDropdownOpen =
      true;

  }


  // =========================================================
  // CLOSE NATIONALITY DROPDOWN
  // =========================================================

  closeNationalityDropdown(): void {

    setTimeout(() => {

      this.nationalityDropdownOpen =
        false;

      this.cdr.markForCheck();

    }, 150);

  }


  // =========================================================
  // SELECT NATIONALITY
  // =========================================================

  selectNationality(
    nationality: string
  ): void {

    this.client.nationality =
      nationality;

    this.nationalitySearch =
      nationality;

    this.nationalityDropdownOpen =
      false;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CHECK CLIENT NAME
  // =========================================================

  checkName(): void {

    const name =
      this.client.nameClient.trim();


    this.nameAlreadyExists =
      false;


    if (!name) {

      return;

    }


    this.checkingName = true;


    this.validationService
      .nameExists(name)
      .subscribe({

        next: exists => {

          this.nameAlreadyExists =
            exists;

          this.checkingName = false;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Error checking client name:',
            error
          );

          this.checkingName = false;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // CHECK EMAIL
  // =========================================================

  checkEmail(): void {

    const email =
      this.client.email.trim();


    this.emailAlreadyExists =
      false;


    if (!email) {

      return;

    }


    if (!this.isValidEmail(email)) {

      return;

    }


    this.checkingEmail = true;


    this.validationService
      .emailExists(email)
      .subscribe({

        next: exists => {

          this.emailAlreadyExists =
            exists;

          this.checkingEmail = false;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Error checking email:',
            error
          );

          this.checkingEmail = false;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // CREATE CLIENT
  // =========================================================

  createClient(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';


    const nameClient =
      this.client.nameClient.trim();

    const email =
      this.client.email.trim();

    const nationality =
      this.client.nationality.trim();


    // =======================================================
    // REQUIRED FIELDS
    // =======================================================

    if (
      !nameClient ||
      !email ||
      !nationality ||
      this.client.budget < 0 ||
      this.client.locationId === null
    ) {

      return;

    }


    // =======================================================
    // EMAIL FORMAT
    // =======================================================

    if (!this.isValidEmail(email)) {

      this.errorMessage =
        'Please enter a valid email address.';

      return;

    }


    // =======================================================
    // NAME UNIQUENESS
    // =======================================================

    if (this.nameAlreadyExists) {

      this.errorMessage =
        'This client name already exists.';

      return;

    }


    // =======================================================
    // EMAIL UNIQUENESS
    // =======================================================

    if (this.emailAlreadyExists) {

      this.errorMessage =
        'This email address is already registered.';

      return;

    }


    // =======================================================
    // PREVENT DOUBLE SUBMISSION
    // =======================================================

    if (this.saving) {

      return;

    }


    // =======================================================
    // LOCATION
    // =======================================================

    const location =
      this.locations.find(
        item =>
          item.id ===
          this.client.locationId
      );


    if (!location) {

      this.errorMessage =
        'Please select a valid location.';

      return;

    }


    // =======================================================
    // SAVE
    // =======================================================

    this.saving = true;


    this.clientService
      .createClient({

        nameClient,

        email,

        nationality,

        budget:
        this.client.budget,

        locationId:
        this.client.locationId,

        locationName:
        location.name

      })
      .subscribe({

        next: () => {

          this.saving = false;

          this.successMessage =
            'Client created successfully.';

          this.cdr.markForCheck();


          setTimeout(() => {

            this.router.navigate([
              '/admin/clients'
            ]);

          }, 800);

        },

        error: error => {

          console.error(
            'Error creating client:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create the client.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // EMAIL VALIDATION
  // =========================================================

  private isValidEmail(
    email: string
  ): boolean {

    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      .test(email);

  }


  // =========================================================
  // EMAIL TEMPLATE VALIDATION
  // =========================================================

  isValidEmailForTemplate(): boolean {

    const email =
      this.client.email.trim();


    if (!email) {

      return false;

    }


    return this.isValidEmail(
      email
    );

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
  // EMAIL INVALID
  // =========================================================

  isEmailInvalid(): boolean {

    if (!this.submitted) {

      return false;

    }


    const email =
      this.client.email.trim();


    return (
      !email ||
      !this.isValidEmail(email)
    );

  }


  // =========================================================
  // LOCATION INVALID
  // =========================================================

  isLocationInvalid(): boolean {

    return this.submitted &&
      this.client.locationId === null;

  }


  // =========================================================
  // NAME INVALID
  // =========================================================

  isNameInvalid(): boolean {

    return (
      this.submitted &&
      (
        !this.client.nameClient.trim() ||
        this.nameAlreadyExists
      )
    );

  }


  // =========================================================
  // RESET
  // =========================================================

  reset(): void {

    this.client = {

      nameClient: '',

      email: '',

      nationality: '',

      budget: 0,

      locationId: null,

      locationName: '',

      locationCountry: ''

    };


    this.nationalitySearch =
      '';

    this.nationalityDropdownOpen =
      false;

    this.submitted =
      false;

    this.saving =
      false;

    this.nameAlreadyExists =
      false;

    this.emailAlreadyExists =
      false;

    this.successMessage =
      '';

    this.errorMessage =
      '';

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    if (this.saving) {

      return;

    }


    this.router.navigate([
      '/admin/creation'
    ]);

  }

}
