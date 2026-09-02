import {
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
  RepairManagementService
} from '../../../services/admin-services/repair-management.service';

import {
  UserLocationValidationService
} from '../../../services/user-location-validation.service';
import {LocationValidation} from '../../../models/location-validation.model';



@Component({
  selector: 'app-add-repair',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatIconModule
  ],

  templateUrl: './add-repair.html',
  styleUrl: './add-repair.css'
})
export class AddRepair implements OnInit {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly router =
    inject(Router);

  private readonly repairService =
    inject(RepairManagementService);

  private readonly validationService =
    inject(UserLocationValidationService);


  // =========================================================
  // FORM
  // =========================================================

  repair = {

    repairName: '',

    email: '',

    role: 'REPAIR',

    locationId: 0,

    locationName: '',

    locationCountry: ''

  };


  // =========================================================
  // LOCATIONS
  // =========================================================

  locations: LocationValidation[] = [];


  // =========================================================
  // STATE
  // =========================================================

  loadingLocations = true;

  saving = false;

  submitted = false;

  checkingName = false;

  checkingEmail = false;

  nameAlreadyExists = false;

  emailAlreadyExists = false;

  successMessage = '';

  errorMessage = '';

  nameErrorMessage = '';

  emailErrorMessage = '';


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

        }

      });

  }


  // =========================================================
  // LOCATION CHANGE
  // =========================================================

  onLocationChange(): void {

    const location =
      this.locations.find(
        item =>
          item.id ===
          this.repair.locationId
      );


    if (!location) {

      this.repair.locationName = '';

      this.repair.locationCountry = '';

      return;

    }


    this.repair.locationName =
      location.name;

    this.repair.locationCountry =
      location.country;

  }


  // =========================================================
  // CHECK REPAIR NAME
  // =========================================================

  checkName(): void {

    const name =
      this.repair.repairName.trim();


    this.nameAlreadyExists = false;

    this.nameErrorMessage = '';


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


          if (exists) {

            this.nameErrorMessage =
              'This repair center name is already used.';

          }

        },

        error: error => {

          console.error(
            'Error checking repair name:',
            error
          );

          this.checkingName = false;

        }

      });

  }


  // =========================================================
  // CHECK EMAIL
  // =========================================================

  checkEmail(): void {

    const email =
      this.repair.email.trim();


    this.emailAlreadyExists = false;

    this.emailErrorMessage = '';


    if (!email) {

      return;

    }


    if (!this.isValidEmail(email)) {

      this.emailErrorMessage =
        'Please enter a valid email address.';

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


          if (exists) {

            this.emailErrorMessage =
              'This email address is already registered.';

          }

        },

        error: error => {

          console.error(
            'Error checking repair email:',
            error
          );

          this.checkingEmail = false;

        }

      });

  }


  // =========================================================
  // CREATE REPAIR
  // =========================================================

  createRepair(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';


    const repairName =
      this.repair.repairName.trim();

    const email =
      this.repair.email.trim();

    const role =
      this.repair.role.trim();


    // =======================================================
    // REQUIRED FIELDS
    // =======================================================

    if (
      !repairName ||
      !email
    ) {

      return;

    }


    // =======================================================
    // EMAIL FORMAT
    // =======================================================

    if (!this.isValidEmail(email)) {

      this.emailErrorMessage =
        'Please enter a valid email address.';

      return;

    }


    // =======================================================
    // NAME UNIQUENESS
    // =======================================================

    if (this.nameAlreadyExists) {

      this.errorMessage =
        'The repair center name is already in use.';

      return;

    }


    // =======================================================
    // EMAIL UNIQUENESS
    // =======================================================

    if (this.emailAlreadyExists) {

      this.errorMessage =
        'The email address is already in use.';

      return;

    }


    // =======================================================
    // LOCATION
    // =======================================================

    if (
      this.repair.locationId <= 0
    ) {

      this.errorMessage =
        'Please select a location.';

      return;

    }


    const selectedLocation =
      this.locations.find(
        location =>
          location.id ===
          this.repair.locationId
      );


    if (!selectedLocation) {

      this.errorMessage =
        'The selected location is invalid.';

      return;

    }


    // =======================================================
    // PREVENT DOUBLE SUBMISSION
    // =======================================================

    if (this.saving) {

      return;

    }


    this.saving = true;


    // =======================================================
    // CREATE
    // =======================================================

    this.repairService
      .createRepair({

        repairName,

        email,

        role,

        locationId:
        selectedLocation.id

      })
      .subscribe({

        next: () => {

          this.saving = false;

          this.successMessage =
            'Repair center created successfully.';


          setTimeout(() => {

            this.router.navigate([
              '/admin/repairs'
            ]);

          }, 800);

        },

        error: error => {

          console.error(
            'Error creating repair center:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create the repair center.';

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
  // TEMPLATE EMAIL VALIDATION
  // =========================================================

  isEmailValidForTemplate(): boolean {

    const email =
      this.repair.email.trim();

    return (
      email.length > 0 &&
      this.isValidEmail(email)
    );

  }


  // =========================================================
  // GENERAL VALIDATION
  // =========================================================

  isInvalid(
    value: string
  ): boolean {

    return this.submitted &&
      !value.trim();

  }


  // =========================================================
  // LOCATION VALIDATION
  // =========================================================

  isLocationInvalid(): boolean {

    return this.submitted &&
      this.repair.locationId <= 0;

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    if (
      this.saving ||
      this.checkingName ||
      this.checkingEmail
    ) {

      return;

    }

    this.router.navigate([
      '/admin/creation'
    ]);

  }

}
