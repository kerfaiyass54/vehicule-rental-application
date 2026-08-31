import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import {
  ClientService
} from '../../../../services/client-services/client.service';

import { CommonModule } from '@angular/common';

import {
  catchError,
  finalize,
  of
} from 'rxjs';

import Keycloak from 'keycloak-js';

import {
  ClientBuyingService
} from '../../../../services/client-services/client-buying.service';

import {
  ClientSubscriptionService
} from '../../../../services/client-services/client-subscription.service';

import {
  ClientVehiculeService
} from '../../../../services/client-services/client-vehicule.service';

import {
  SupplierInfo
} from '../../../models/supplier-info.model';

import {
  VehiculeSearchDTO
} from '../../../models/vehicule-search.model';

import {
  Buying
} from '../../../models/buying.model';

import {
  SubscriptionInfo
} from '../../../models/subscription-info.model';


@Component({
  selector: 'app-buy-review',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './buy-review.html',

  styleUrl: './buy-review.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BuyReview implements OnChanges {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly buyingService =
    inject(ClientBuyingService);

  private readonly clientService =
    inject(ClientService);

  private readonly subscriptionService =
    inject(ClientSubscriptionService);

  private readonly vehiculeService =
    inject(ClientVehiculeService);

  private readonly keycloak =
    inject(Keycloak);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // INPUTS
  // =========================================================

  @Input()
  supplier: SupplierInfo | null = null;


  @Input()
  supplierSubscribed = false;


  @Input()
  vehicule: VehiculeSearchDTO | null = null;


  @Input()
  period = 1;


  @Input()
  renew = false;


  // =========================================================
  // OUTPUTS
  // =========================================================

  @Output()
  confirmed =
    new EventEmitter<Buying>();


  // =========================================================
  // STATE
  // =========================================================

  confirming = false;


  buying: Buying | null = null;


  /**
   * Client email retrieved from Keycloak.
   */
  clientEmail = '';


  /**
   * Supplier email retrieved from SupplierInfo.
   */
  supplierEmail = '';


  /**
   * Current subscription between
   * this client and this supplier.
   */
  subscription: SubscriptionInfo | null = null;


  /**
   * Subscription reduction percentage.
   *
   * Example:
   *
   * 0  = no reduction
   * 5  = 5%
   * 20 = 20%
   */
  reduction = 0;


  /**
   * Vehicle price × rental period.
   *
   * Used as the original/base amount
   * displayed before reduction.
   */
  basePrice = 0;


  /**
   * Amount removed by subscription reduction.
   */
  discountAmount = 0;


  /**
   * Final price returned by backend.
   */
  calculatedPrice: number | null = null;


  /**
   * Loading state while subscription
   * and final price information is retrieved.
   */
  calculating = false;


  // =========================================================
  // CONFIRMATION DIALOG
  // =========================================================

  showConfirmDialog = false;


  // =========================================================
  // NOTIFICATION
  // =========================================================

  notificationVisible = false;

  notificationType:
    | 'success'
    | 'error'
    | 'warning' = 'error';

  notificationTitle = '';

  notificationMessage = '';


  private notificationTimer:
    ReturnType<typeof setTimeout> | null = null;


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    /**
     * Whenever the selected vehicle,
     * supplier or rental period changes,
     * refresh the price calculation.
     */
    if (
      changes['vehicule'] ||
      changes['supplier'] ||
      changes['period'] ||
      changes['supplierSubscribed']
    ) {

      this.loadRentalPricing();

    }

  }


  // =========================================================
  // LOAD RENTAL PRICING
  // =========================================================

  private loadRentalPricing(): void {

    /**
     * Reset values first.
     */
    this.subscription = null;

    this.reduction = 0;

    this.basePrice = 0;

    this.discountAmount = 0;

    this.calculatedPrice = null;


    // ---------------------------------------------------------
    // VALIDATE VEHICLE
    // ---------------------------------------------------------

    if (!this.vehicule) {

      return;

    }


    // ---------------------------------------------------------
    // VALIDATE PERIOD
    // ---------------------------------------------------------

    if (
      !this.period ||
      this.period < 1
    ) {

      this.showNotification(
        'warning',
        'Invalid rental period',
        'The rental period must be at least 1 day.'
      );

      return;

    }


    // ---------------------------------------------------------
    // GET CLIENT EMAIL
    // ---------------------------------------------------------

    const clientEmail =
      this.getClientEmail();


    if (!clientEmail) {

      this.showNotification(
        'error',
        'Authentication error',
        'The client email could not be retrieved from Keycloak.'
      );

      return;

    }


    this.clientEmail =
      clientEmail;


    // ---------------------------------------------------------
    // GET SUPPLIER EMAIL
    // ---------------------------------------------------------

    if (!this.supplier) {

      this.showNotification(
        'warning',
        'Supplier missing',
        'No supplier has been selected.'
      );

      return;

    }


    const supplierEmail =
      this.supplier.email;


    if (!supplierEmail) {

      this.showNotification(
        'error',
        'Supplier email missing',
        'The selected supplier does not have a valid email address.'
      );

      return;

    }


    this.supplierEmail =
      supplierEmail;


    // ---------------------------------------------------------
    // BASE PRICE
    // ---------------------------------------------------------

    this.basePrice =
      this.vehicule.price * this.period;


    // ---------------------------------------------------------
    // SUPPLIER NOT SUBSCRIBED
    // ---------------------------------------------------------

    /**
     * If the supplier is not subscribed,
     * there is no subscription reduction.
     *
     * We still use the backend total-price
     * endpoint with reduction = 0.
     */

    if (!this.supplierSubscribed) {

      this.calculateTotalPrice(0);

      return;

    }


    // ---------------------------------------------------------
    // LOAD SUBSCRIPTION
    // ---------------------------------------------------------

    this.calculating = true;

    this.cdr.markForCheck();


    this.subscriptionService
      .getSubscriptionDetails(
        clientEmail,
        supplierEmail
      )
      .pipe(
        catchError(error => {

          console.error(
            'Unable to retrieve subscription details:',
            error
          );


          /**
           * A missing subscription is not considered
           * a fatal error.
           *
           * The rental simply has no reduction.
           */

          if (error?.status === 404) {

            return of(null);

          }


          this.showNotification(
            'error',
            'Subscription error',
            'Unable to retrieve your subscription information.'
          );

          return of(null);

        }),

        finalize(() => {

          this.calculating = false;

          this.cdr.markForCheck();

        })
      )
      .subscribe({

        next: subscription => {

          this.subscription =
            subscription;


          // ---------------------------------------------------
          // NO SUBSCRIPTION
          // ---------------------------------------------------

          if (!subscription) {

            /**
             * No subscription with this supplier.
             *
             * Therefore:
             * reduction = 0
             */

            this.calculateTotalPrice(0);

            return;

          }


          // ---------------------------------------------------
          // GET REDUCTION
          // ---------------------------------------------------

          this.loadReduction(
            subscription
          );

        }

      });

  }


  // =========================================================
  // LOAD REDUCTION
  // =========================================================

  private loadReduction(
    subscription: SubscriptionInfo
  ): void {

    this.subscriptionService
      .getReduction(
        subscription.type
      )
      .pipe(
        finalize(() => {

          this.cdr.markForCheck();

        })
      )
      .subscribe({

        next: reduction => {

          // Backend returns:
          // 15 = 15% reduction

          this.reduction =
            Math.max(
              0,
              Math.min(
                100,
                Number(reduction) || 0
              )
            );


          // Calculate final price manually
          this.calculateTotalPrice(
            this.reduction
          );

        },

        error: error => {

          console.error(
            'Unable to retrieve reduction:',
            error
          );

          this.reduction = 0;

          this.calculateTotalPrice(0);

          this.showNotification(
            'warning',
            'Reduction unavailable',
            'The subscription was found, but its reduction could not be loaded.'
          );

        }

      });

  }




  private calculateTotalPrice(
    reduction: number
  ): void {

    // ---------------------------------------------------------
    // VALIDATE VEHICLE
    // ---------------------------------------------------------

    if (!this.vehicule) {

      this.showNotification(
        'error',
        'Vehicle missing',
        'No vehicle has been selected.'
      );

      return;

    }


    // ---------------------------------------------------------
    // VALIDATE PERIOD
    // ---------------------------------------------------------

    if (
      !this.period ||
      this.period < 1
    ) {

      this.showNotification(
        'warning',
        'Invalid rental period',
        'The rental period must be at least 1 day.'
      );

      return;

    }


    // ---------------------------------------------------------
    // NORMALIZE REDUCTION
    // ---------------------------------------------------------

    this.reduction =
      Math.max(
        0,
        Math.min(
          100,
          Number(reduction) || 0
        )
      );


    // ---------------------------------------------------------
    // BASE PRICE
    // ---------------------------------------------------------

    this.basePrice =
      this.vehicule.price * this.period;


    // ---------------------------------------------------------
    // FINAL PRICE
    // ---------------------------------------------------------

    this.calculatedPrice =
      this.basePrice *
      this.reduction /
      100;


    // ---------------------------------------------------------
    // DISCOUNT AMOUNT
    // ---------------------------------------------------------

    this.discountAmount =
      this.basePrice -
      this.calculatedPrice;


    // ---------------------------------------------------------
    // ROUND VALUES
    // ---------------------------------------------------------

    this.basePrice =
      Math.round(
        this.basePrice * 100
      ) / 100;


    this.discountAmount =
      Math.round(
        this.discountAmount * 100
      ) / 100;


    this.calculatedPrice =
      Math.round(
        this.calculatedPrice * 100
      ) / 100;


    this.cdr.markForCheck();

  }


  // =========================================================
  // CLIENT EMAIL FROM KEYCLOAK
  // =========================================================

  private getClientEmail(): string | null {

    const token =
      this.keycloak.tokenParsed;


    const email =
      token?.['email'];


    if (
      !email ||
      typeof email !== 'string' ||
      !email.trim()
    ) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      return null;

    }


    return email.trim();

  }


  // =========================================================
  // OPEN CONFIRMATION DIALOG
  // =========================================================

  openConfirmDialog(): void {

    if (!this.vehicule) {

      this.showNotification(
        'warning',
        'Vehicle missing',
        'No vehicle has been selected.'
      );

      return;

    }


    if (!this.clientEmail) {

      this.showNotification(
        'error',
        'Authentication error',
        'Client email could not be retrieved from Keycloak.'
      );

      return;

    }


    if (
      this.calculatedPrice === null
    ) {

      this.showNotification(
        'warning',
        'Price unavailable',
        'The rental price is still being calculated.'
      );

      return;

    }


    if (this.calculating) {

      this.showNotification(
        'warning',
        'Calculation in progress',
        'Please wait until the final price has been calculated.'
      );

      return;

    }


    this.showConfirmDialog =
      true;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE CONFIRMATION DIALOG
  // =========================================================

  closeConfirmDialog(): void {

    if (this.confirming) {

      return;

    }


    this.showConfirmDialog =
      false;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CONFIRM PURCHASE
  // =========================================================

  confirmPurchase(): void {

    if (this.confirming) {

      return;

    }


    // ---------------------------------------------------------
    // VEHICLE
    // ---------------------------------------------------------

    if (!this.vehicule) {

      this.showNotification(
        'error',
        'Vehicle missing',
        'No vehicle has been selected.'
      );

      return;

    }


    if (!this.vehicule.idVehicule) {

      this.showNotification(
        'error',
        'Vehicle ID missing',
        'The selected vehicle does not have a valid ID.'
      );

      return;

    }


    // ---------------------------------------------------------
    // CLIENT EMAIL
    // ---------------------------------------------------------

    const clientEmail =
      this.getClientEmail();


    if (!clientEmail) {

      this.showNotification(
        'error',
        'Authentication error',
        'Client email could not be retrieved from Keycloak.'
      );

      return;

    }


    this.clientEmail =
      clientEmail;


    // ---------------------------------------------------------
    // PERIOD
    // ---------------------------------------------------------

    if (
      !this.period ||
      this.period < 1
    ) {

      this.showNotification(
        'error',
        'Invalid rental period',
        'The rental period must be at least 1 day.'
      );

      return;

    }


    // ---------------------------------------------------------
    // PRICE
    // ---------------------------------------------------------

    if (
      this.calculatedPrice === null ||
      this.calculating
    ) {

      this.showNotification(
        'warning',
        'Price unavailable',
        'Please wait until the rental price has been calculated.'
      );

      return;

    }


    // ---------------------------------------------------------
    // CLOSE DIALOG
    // ---------------------------------------------------------

    this.showConfirmDialog =
      false;


    // ---------------------------------------------------------
    // CONFIRM
    // ---------------------------------------------------------

    this.confirming =
      true;


    this.buyingService
      .addBuying(
        this.vehicule.idVehicule,
        clientEmail,
        this.period,
        this.renew
      )
      .pipe(
        finalize(() => {

          this.confirming =
            false;

          this.cdr.markForCheck();

        })
      )
      .subscribe({

        // -----------------------------------------------------
        // SUCCESS
        // -----------------------------------------------------

        next: buying => {

          this.buying =
            buying;

          this.confirmed.emit(
            buying
          );


          // =========================================================
          // REDUCE CLIENT BUDGET
          // =========================================================

          const price =
            this.calculatedPrice;

          if (
            price === null ||
            price <= 0
          ) {

            this.showNotification(
              'warning',
              'Rental created',
              'The rental was created, but the budget could not be updated.'
            );

            this.cdr.markForCheck();

            return;

          }


          this.clientService
            .reduceBudget(
              clientEmail,
              price
            )
            .pipe(
              finalize(() => {

                this.cdr.markForCheck();

              })
            )
            .subscribe({

              next: () => {

                this.showNotification(
                  'success',
                  'Rental confirmed',
                  `Rental created successfully. ${this.formatPrice(price)} was deducted from your budget.`
                );

                this.cdr.markForCheck();

              },

              error: error => {

                console.error(
                  'Rental created but budget reduction failed:',
                  error
                );

                this.showNotification(
                  'warning',
                  'Rental created',
                  'The rental was created successfully, but your budget could not be updated.'
                );

                this.cdr.markForCheck();

              }

            });

        },


        // -----------------------------------------------------
        // ERROR
        // -----------------------------------------------------

        error: error => {

          console.error(
            'Unable to confirm vehicle rental:',
            error
          );


          const message =
            error?.error?.message ??
            'Unable to complete the rental. Please try again.';


          this.showNotification(
            'error',
            'Rental failed',
            message
          );


          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // NOTIFICATION
  // =========================================================

  private showNotification(
    type:
      | 'success'
      | 'error'
      | 'warning',

    title: string,

    message: string
  ): void {

    this.notificationType =
      type;

    this.notificationTitle =
      title;

    this.notificationMessage =
      message;

    this.notificationVisible =
      true;


    // ---------------------------------------------------------
    // CLEAR PREVIOUS TIMER
    // ---------------------------------------------------------

    if (
      this.notificationTimer
    ) {

      clearTimeout(
        this.notificationTimer
      );

    }


    // ---------------------------------------------------------
    // AUTO HIDE
    // ---------------------------------------------------------

    this.notificationTimer =
      setTimeout(() => {

        this.notificationVisible =
          false;

        this.cdr.markForCheck();

      }, 5000);


    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE NOTIFICATION
  // =========================================================

  closeNotification(): void {

    this.notificationVisible =
      false;


    if (
      this.notificationTimer
    ) {

      clearTimeout(
        this.notificationTimer
      );

      this.notificationTimer =
        null;

    }


    this.cdr.markForCheck();

  }


  // =========================================================
  // PRICE FORMAT
  // =========================================================

  formatPrice(
    price: number | null
  ): string {

    if (
      price === null ||
      price === undefined
    ) {

      return '—';

    }


    return new Intl.NumberFormat(
      'en-US',
      {
        style: 'currency',
        currency: 'EUR',
        maximumFractionDigits: 2
      }
    ).format(price);

  }


  // =========================================================
  // VEHICLE TOTAL
  // =========================================================

  get vehicleTotal(): number {

    return this.basePrice;

  }


  // =========================================================
  // DISCOUNT LABEL
  // =========================================================

  get discountLabel(): string {

    return `${this.reduction}%`;

  }

}
