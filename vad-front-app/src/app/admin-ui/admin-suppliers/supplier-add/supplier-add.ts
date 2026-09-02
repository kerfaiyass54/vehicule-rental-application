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
  SupplierManagementService
} from '../../../services/admin-services/supplier-management.service';

import {
  UserLocationValidationService
} from '../../../services/user-location-validation.service';
import {NATIONALITIES} from '../../constants/nationalities';




@Component({
  selector: 'app-supplier-add',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatIconModule
  ],

  templateUrl: './supplier-add.html',
  styleUrl: './supplier-add.css'
})
export class SupplierAdd {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly router =
    inject(Router);

  private readonly supplierService =
    inject(SupplierManagementService);

  private readonly validationService =
    inject(UserLocationValidationService);


  // =========================================================
  // NATIONALITIES
  // =========================================================

  readonly nationalities =
    NATIONALITIES;


  // =========================================================
  // FORM
  // =========================================================

  supplier = {

    suppName: '',

    email: '',

    nationality: '',

    experience: 0

  };


  // =========================================================
  // NATIONALITY SEARCH
  // =========================================================

  nationalitySearch = '';

  nationalityDropdownOpen = false;


  // =========================================================
  // STATE
  // =========================================================

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
  // FILTERED NATIONALITIES
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

    if (this.saving) {

      return;

    }

    this.nationalityDropdownOpen = true;

  }


  // =========================================================
  // CLOSE NATIONALITY DROPDOWN
  // =========================================================

  closeNationalityDropdown(): void {

    setTimeout(() => {

      this.nationalityDropdownOpen =
        false;

    }, 150);

  }


  // =========================================================
  // NATIONALITY INPUT
  // =========================================================

  onNationalityInput(): void {

    this.nationalityDropdownOpen =
      true;

    /*
     * Typing means the user has not selected
     * a nationality yet.
     */

    this.supplier.nationality = '';

  }


  // =========================================================
  // SELECT NATIONALITY
  // =========================================================

  selectNationality(
    nationality: string
  ): void {

    this.supplier.nationality =
      nationality;

    this.nationalitySearch =
      nationality;

    this.nationalityDropdownOpen =
      false;

  }


  // =========================================================
  // CLEAR NATIONALITY
  // =========================================================

  clearNationality(): void {

    if (this.saving) {

      return;

    }

    this.supplier.nationality =
      '';

    this.nationalitySearch =
      '';

    this.nationalityDropdownOpen =
      true;

  }


  // =========================================================
  // CREATE SUPPLIER
  // =========================================================

  createSupplier(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';

    this.nameErrorMessage = '';

    this.emailErrorMessage = '';


    // =======================================================
    // CLEAN VALUES
    // =======================================================

    const suppName =
      this.supplier.suppName.trim();

    const email =
      this.supplier.email.trim();

    const nationality =
      this.supplier.nationality.trim();

    const experience =
      Number(this.supplier.experience);


    // =======================================================
    // REQUIRED FIELDS
    // =======================================================

    if (
      !suppName ||
      !email ||
      !nationality
    ) {

      this.errorMessage =
        'Please fill in all required fields.';

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
    // EXPERIENCE
    // =======================================================

    if (
      !Number.isFinite(experience) ||
      experience < 0
    ) {

      this.errorMessage =
        'Experience must be zero or greater.';

      return;

    }


    // =======================================================
    // PREVENT DOUBLE SUBMISSION
    // =======================================================

    if (this.saving) {

      return;

    }


    // =======================================================
    // RESET EXISTENCE FLAGS
    // =======================================================

    this.nameAlreadyExists = false;

    this.emailAlreadyExists = false;


    this.saving = true;


    // =======================================================
    // CHECK NAME
    // =======================================================

    this.checkingName = true;

    this.validationService
      .nameExists(suppName)
      .subscribe({

        next: nameExists => {

          this.checkingName = false;


          if (nameExists) {

            this.nameAlreadyExists =
              true;

            this.nameErrorMessage =
              'This supplier name is already used by another user.';

            this.saving = false;

            return;

          }


          // -------------------------------------------------
          // NAME IS AVAILABLE -> CHECK EMAIL
          // -------------------------------------------------

          this.checkEmailAndCreate(

            suppName,

            email,

            nationality,

            experience

          );

        },

        error: error => {

          console.error(
            'Error checking supplier name:',
            error
          );

          this.checkingName = false;

          this.saving = false;

          this.errorMessage =
            'Unable to verify supplier name.';

        }

      });

  }


  // =========================================================
  // CHECK EMAIL
  // =========================================================

  private checkEmailAndCreate(

    suppName: string,

    email: string,

    nationality: string,

    experience: number

  ): void {

    this.checkingEmail = true;


    this.validationService
      .emailExists(email)
      .subscribe({

        next: emailExists => {

          this.checkingEmail = false;


          if (emailExists) {

            this.emailAlreadyExists =
              true;

            this.emailErrorMessage =
              'This email address is already registered.';

            this.saving = false;

            return;

          }


          // -------------------------------------------------
          // BOTH NAME AND EMAIL ARE AVAILABLE
          // -------------------------------------------------

          this.saveSupplier(

            suppName,

            email,

            nationality,

            experience

          );

        },

        error: error => {

          console.error(
            'Error checking supplier email:',
            error
          );

          this.checkingEmail = false;

          this.saving = false;

          this.errorMessage =
            'Unable to verify supplier email.';

        }

      });

  }


  // =========================================================
  // SAVE SUPPLIER
  // =========================================================

  private saveSupplier(

    suppName: string,

    email: string,

    nationality: string,

    experience: number

  ): void {


    // =======================================================
    // SUPPLIER ROLE
    // =======================================================

    const role =
      'SUPPLIER';


    // =======================================================
    // REQUEST OBJECT
    // =======================================================

    const supplierCreation = {

      id: 0,

      suppName,

      email,

      nationality,

      experience,

      role

    };


    // =======================================================
    // CREATE
    // =======================================================

    this.supplierService
      .createSupplier(
        supplierCreation
      )
      .subscribe({

        next: () => {

          this.saving = false;

          this.successMessage =
            'Supplier created successfully.';


          setTimeout(() => {

            this.router.navigate([
              '/admin/suppliers'
            ]);

          }, 800);

        },

        error: error => {

          console.error(
            'Error creating supplier:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create the supplier.';

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
  // EMAIL VALIDATION FOR TEMPLATE
  // =========================================================

  isValidEmailForTemplate(
    email: string
  ): boolean {

    return this.isValidEmail(
      email.trim()
    );

  }


  // =========================================================
  // CHECK EMAIL FROM TEMPLATE
  // =========================================================

  checkEmailForTemplate(): void {

    const email =
      this.supplier.email.trim();


    if (
      !email ||
      !this.isValidEmail(email)
    ) {

      this.emailAlreadyExists =
        false;

      this.emailErrorMessage =
        '';

      return;

    }


    this.checkingEmail = true;

    this.validationService
      .emailExists(email)
      .subscribe({

        next: exists => {

          this.emailAlreadyExists =
            exists;

          this.emailErrorMessage =
            exists
              ? 'This email address is already registered.'
              : '';

          this.checkingEmail = false;

        },

        error: error => {

          console.error(
            'Error checking supplier email:',
            error
          );

          this.checkingEmail = false;

        }

      });

  }


  // =========================================================
  // CHECK SUPPLIER NAME
  // =========================================================

  checkName(): void {

    const name =
      this.supplier.suppName.trim();


    this.nameAlreadyExists =
      false;

    this.nameErrorMessage =
      '';


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

          this.nameErrorMessage =
            exists
              ? 'This supplier name is already used by another user.'
              : '';

          this.checkingName = false;

        },

        error: error => {

          console.error(
            'Error checking supplier name:',
            error
          );

          this.checkingName = false;

        }

      });

  }


  // =========================================================
  // REQUIRED FIELD VALIDATION
  // =========================================================

  isInvalid(
    value: string
  ): boolean {

    return this.submitted &&
      !value.trim();

  }


  // =========================================================
  // EXPERIENCE VALIDATION
  // =========================================================

  isExperienceInvalid(): boolean {

    const experience =
      Number(this.supplier.experience);

    return this.submitted &&
      (
        !Number.isFinite(experience) ||
        experience < 0
      );

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
