import { Component } from '@angular/core';
import { DecimalPipe } from '@angular/common';

import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { SearchVehicule } from './search-vehicule/search-vehicule';
import { BuyVehiculeForm } from './buy-vehicule-form/buy-vehicule-form';
import { BuyReview } from './buy-review/buy-review';
import { SearchSupplier } from './search-supplier/search-supplier';


@Component({
  selector: 'app-buy-vehicule',
  standalone: true,

  imports: [
    DecimalPipe,

    MatStepperModule,
    MatButtonModule,
    MatIconModule,

    SearchSupplier,
    SearchVehicule,
    BuyVehiculeForm,
    BuyReview
  ],

  templateUrl: './buy-vehicule.html',
  styleUrl: './buy-vehicule.css'
})
export class BuyVehicule {

  // =========================================================
  // STEPPER STATE
  // =========================================================

  currentStep = 0;


  // =========================================================
  // PURCHASE STATE
  // =========================================================

  selectedSupplier: any = null;

  selectedSupplierSubscribed = false;

  selectedVehicule: any = null;

  period = 0;

  renew = false;


  // =========================================================
  // STEP 1 - SUPPLIER
  // =========================================================

  onSupplierSelected(
    event: {
      supplier: any;
      subscribed: boolean;
    }
  ): void {

    this.selectedSupplier = event.supplier;

    this.selectedSupplierSubscribed =
      event.subscribed;

  }


  // =========================================================
  // STEP 2 - VEHICLE
  // =========================================================

  onVehiculeSelected(
    vehicule: any
  ): void {

    this.selectedVehicule = vehicule;

  }


  // =========================================================
  // STEP 3 - RENTAL FORM
  // =========================================================

  onFormSubmitted(
    event: {
      period: number;
      renew: boolean;
    }
  ): void {

    this.period = event.period;

    this.renew = event.renew;

  }


  // =========================================================
  // VALIDATION
  // =========================================================

  get supplierSelected(): boolean {

    return this.selectedSupplier !== null;

  }


  get vehiculeSelected(): boolean {

    return this.selectedVehicule !== null;

  }


  get formCompleted(): boolean {

    return this.period > 0;

  }


  // =========================================================
  // RESET
  // =========================================================

  reset(): void {

    this.selectedSupplier = null;

    this.selectedSupplierSubscribed = false;

    this.selectedVehicule = null;

    this.period = 0;

    this.renew = false;

    this.currentStep = 0;

  }

}
