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


  // =========================================================
  // FORM
  // =========================================================

  supplier = {

    suppName: '',

    email: '',

    nationality: '',

    experience: 0,

    role: ''

  };


  // =========================================================
  // STATE
  // =========================================================

  saving = false;

  submitted = false;

  successMessage = '';

  errorMessage = '';


  // =========================================================
  // CREATE SUPPLIER
  // =========================================================

  createSupplier(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';


    // -------------------------------------------------------
    // CLEAN VALUES
    // -------------------------------------------------------

    const suppName =
      this.supplier.suppName.trim();

    const email =
      this.supplier.email.trim();

    const nationality =
      this.supplier.nationality.trim();

    const role =
      this.supplier.role.trim();

    const experience =
      Number(this.supplier.experience);


    // -------------------------------------------------------
    // REQUIRED FIELDS
    // -------------------------------------------------------

    if (
      !suppName ||
      !email ||
      !nationality ||
      !role
    ) {

      this.errorMessage =
        'Please fill in all required fields.';

      return;

    }


    // -------------------------------------------------------
    // EMAIL VALIDATION
    // -------------------------------------------------------

    if (!this.isValidEmail(email)) {

      this.errorMessage =
        'Please enter a valid email address.';

      return;

    }


    // -------------------------------------------------------
    // EXPERIENCE VALIDATION
    // -------------------------------------------------------

    if (
      !Number.isFinite(experience) ||
      experience < 0
    ) {

      this.errorMessage =
        'Experience must be a positive number or zero.';

      return;

    }


    // -------------------------------------------------------
    // PREVENT DOUBLE SUBMISSION
    // -------------------------------------------------------

    if (this.saving) {

      return;

    }


    this.saving = true;


    // =======================================================
    // PERFECT REQUEST OBJECT
    // =======================================================

    const supplierCreation = {

      id: 0,

      suppName,

      email,

      nationality,

      experience,

      role

    };


    console.log(
      'Creating supplier:',
      supplierCreation
    );


    // -------------------------------------------------------
    // API REQUEST
    // -------------------------------------------------------

    this.supplierService
      .createSupplier(
        supplierCreation
      )
      .subscribe({

        next: () => {

          this.saving = false;

          this.errorMessage = '';

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
  // CANCEL
  // =========================================================

  cancel(): void {

    if (this.saving) {

      return;

    }

    this.router.navigate([
      '/admin/suppliers'
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

  isValidEmailForTemplate(
    email: string
  ): boolean {

    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      .test(email);

  }




  isExperienceInvalid(): boolean {

    return this.submitted &&
      (
        !Number.isFinite(
          Number(this.supplier.experience)
        ) ||
        Number(this.supplier.experience) < 0
      );

  }

}
