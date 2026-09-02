import {
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  MatCardModule
} from '@angular/material/card';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  SupplierDetails
} from '../models/supplier-details.model';

import {
  SupplierDetailsService
} from '../../services/supplier-services/supplier-details';


@Component({

  selector:
    'app-supplier-hero-page',

  standalone:
    true,

  imports: [

    CommonModule,

    MatCardModule,

    MatIconModule,

    MatProgressSpinnerModule

  ],

  templateUrl:
    './supplier-hero-page.html',

  styleUrl:
    './supplier-hero-page.css'

})
export class SupplierHeroPage
  implements OnInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly supplierService =
    inject(SupplierDetailsService);


  // =========================================================
  // SUPPLIER
  // =========================================================

  readonly supplier =
    signal<SupplierDetails | null>(null);


  // =========================================================
  // STATE
  // =========================================================

  readonly loading =
    signal(true);

  readonly error =
    signal(false);


  // =========================================================
  // SUPPLIER EMAIL
  // =========================================================

  readonly email =
    signal('');


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadSupplier();

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    /*
     * Nothing to clean up.
     *
     * Dashboard/chart logic has been moved
     * to SupplierDashboard.
     */

  }


  // =========================================================
  // LOAD SUPPLIER
  // =========================================================

  private loadSupplier(): void {

    const token =
      this.keycloak.tokenParsed;


    const supplierEmail =
      token?.['email'] ?? '';


    // =======================================================
    // EMAIL NOT FOUND
    // =======================================================

    if (!supplierEmail) {

      this.error.set(true);

      this.loading.set(false);

      return;

    }


    this.email.set(
      supplierEmail
    );


    this.loading.set(true);

    this.error.set(false);


    // =======================================================
    // GET SUPPLIER DETAILS
    // =======================================================

    this.supplierService

      .getSupplierDetails(
        supplierEmail
      )

      .subscribe({

        next: details => {

          this.supplier.set(
            details
          );

          this.loading.set(false);

          this.error.set(false);

        },

        error: error => {

          console.error(
            'Unable to load supplier details:',
            error
          );

          this.supplier.set(null);

          this.loading.set(false);

          this.error.set(true);

        }

      });

  }

}
