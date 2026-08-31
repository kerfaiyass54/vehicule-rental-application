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
  RepairManagementService
} from '../../../services/admin-services/repair-management.service';


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
export class AddRepair {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly router =
    inject(Router);

  private readonly repairService =
    inject(RepairManagementService);


  // =========================================================
  // FORM
  // =========================================================

  repair = {

    repairName: '',

    email: '',

    role: '',

    locationId: 0

  };


  // =========================================================
  // STATE
  // =========================================================

  saving = false;

  submitted = false;

  successMessage = '';

  errorMessage = '';


  // =========================================================
  // CREATE REPAIR
  // =========================================================

  createRepair(): void {

    this.submitted = true;

    this.successMessage = '';

    this.errorMessage = '';


    // ---------------------------------------------------------
    // CLEAN VALUES
    // ---------------------------------------------------------

    const repairName =
      this.repair.repairName.trim();

    const email =
      this.repair.email.trim();

    const role =
      this.repair.role.trim();


    // ---------------------------------------------------------
    // BASIC VALIDATION
    // ---------------------------------------------------------

    if (
      !repairName ||
      !email ||
      !role
    ) {

      return;

    }


    // ---------------------------------------------------------
    // LOCATION VALIDATION
    // ---------------------------------------------------------

    if (
      this.repair.locationId <= 0
    ) {

      this.errorMessage =
        'Please enter a valid location ID.';

      return;

    }


    // ---------------------------------------------------------
    // PREVENT DOUBLE SUBMISSION
    // ---------------------------------------------------------

    if (this.saving) {

      return;

    }


    this.saving = true;


    // =========================================================
    // API CALL
    // =========================================================

    this.repairService
      .createRepair({

        repairName,

        email,

        role,

        locationId:
        this.repair.locationId

      })
      .subscribe({

        // -----------------------------------------------------
        // SUCCESS
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // ERROR
        // -----------------------------------------------------

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
  // CANCEL
  // =========================================================

  cancel(): void {

    if (this.saving) {

      return;

    }

    this.router.navigate([
      '/admin/repairs'
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


  isLocationInvalid(): boolean {

    return this.submitted &&
      this.repair.locationId <= 0;

  }

}
