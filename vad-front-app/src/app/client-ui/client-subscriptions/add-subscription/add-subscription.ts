import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ViewChild,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  MatStepper,
  MatStepperModule
} from '@angular/material/stepper';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import Keycloak from 'keycloak-js';

import {
  finalize
} from 'rxjs';

import {
  ClientSubscriptionService
} from '../../../services/client-services/client-subscription.service';

import {
  SupplierInfo
} from '../../models/supplier-info.model';

import {
  SubscriptionInfo
} from '../../models/subscription-info.model';

import {
  SubscriptionType
} from '../../enums/subscription-type';

import {
  SearchSupplier
} from './search-supplier/search-supplier';

import {
  SubscriptionForm
} from './subscription-form/subscription-form';

import {
  SubscriptionReview
} from './subscription-review/subscription-review';


@Component({
  selector: 'app-add-subscription',

  standalone: true,

  imports: [
    CommonModule,

    MatStepperModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,

    SearchSupplier,
    SubscriptionForm,
    SubscriptionReview
  ],

  templateUrl: './add-subscription.html',

  styleUrl: './add-subscription.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddSubscription {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly subscriptionService =
    inject(ClientSubscriptionService);

  private readonly keycloak =
    inject(Keycloak);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // STEPPER
  // =========================================================

  @ViewChild('stepper')
  stepper!: MatStepper;


  // =========================================================
  // STATE
  // =========================================================

  currentStep = 0;


  selectedSupplier:
    SupplierInfo | null = null;


  selectedSubscriptionType:
    SubscriptionType | null = null;


  selectedPrice = 0;


  selectedReduction = 0;


  subscription:
    SubscriptionInfo | null = null;


  submitting = false;


  completed = false;


  errorMessage = '';


  // =========================================================
  // SUPPLIER SELECTED
  // =========================================================

  onSupplierSelected(
    supplier: SupplierInfo
  ): void {

    this.selectedSupplier =
      supplier;

    /*
     * If the user changes supplier,
     * everything after supplier must be reset.
     */

    this.selectedSubscriptionType =
      null;

    this.selectedPrice =
      0;

    this.selectedReduction =
      0;

    this.subscription =
      null;

    this.errorMessage =
      '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // SUBSCRIPTION SELECTED
  // =========================================================

  onSubscriptionSelected(
    selection: {
      type: SubscriptionType;
      price: number;
      reduction: number;
    }
  ): void {

    this.selectedSubscriptionType =
      selection.type;

    this.selectedPrice =
      selection.price;

    this.selectedReduction =
      selection.reduction;

    /*
     * The review must be rebuilt
     * when the plan changes.
     */

    this.subscription =
      null;

    this.errorMessage =
      '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // STEP VALIDATION
  // =========================================================

  get supplierSelected(): boolean {

    return this.selectedSupplier !== null;

  }


  get subscriptionSelected(): boolean {

    return this.selectedSubscriptionType !== null;

  }


  get reviewReady(): boolean {

    return (
      this.selectedSupplier !== null &&
      this.selectedSubscriptionType !== null
    );

  }


  // =========================================================
  // STEPPER CHANGE
  // =========================================================

  onStepChange(
    index: number
  ): void {

    this.currentStep =
      index;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CREATE SUBSCRIPTION OBJECT
  // =========================================================

  private buildSubscription():
    SubscriptionInfo | null {

    if (
      !this.selectedSupplier ||
      !this.selectedSubscriptionType
    ) {

      return null;

    }


    const token =
      this.keycloak.tokenParsed;


    const email =
      token?.['email'] as string | undefined;


    if (
      !email ||
      !email.trim()
    ) {

      this.errorMessage =
        'Unable to retrieve your email address.';

      return null;

    }


    return {

      type:
      this.selectedSubscriptionType,

      dateStart:
        new Date().toISOString(),

      reduce:
      this.selectedReduction,

      price:
      this.selectedPrice,

      idSupplier:
      this.selectedSupplier.idSupp,

      clientEmail:
        email.trim()

    } as SubscriptionInfo;

  }


  // =========================================================
  // CONFIRM SUBSCRIPTION
  // =========================================================

  confirmSubscription(): void {

    if (this.submitting) {

      return;

    }


    const newSubscription =
      this.buildSubscription();


    if (!newSubscription) {

      this.cdr.markForCheck();

      return;

    }


    this.errorMessage =
      '';

    this.submitting =
      true;


    this.subscriptionService
      .addSubscription(newSubscription)
      .pipe(

        finalize(() => {

          this.submitting =
            false;

          this.cdr.markForCheck();

        })

      )
      .subscribe({

        next: createdSubscription => {

          this.subscription =
            createdSubscription;

          this.completed =
            true;

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to create subscription:',
            error
          );

          this.errorMessage =
            'Unable to create the subscription. Please try again.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // RESET
  // =========================================================

  reset(): void {

    this.selectedSupplier =
      null;

    this.selectedSubscriptionType =
      null;

    this.selectedPrice =
      0;

    this.selectedReduction =
      0;

    this.subscription =
      null;

    this.submitting =
      false;

    this.completed =
      false;

    this.errorMessage =
      '';

    this.currentStep =
      0;

    if (this.stepper) {

      this.stepper.reset();

    }

    this.cdr.markForCheck();

  }

}
