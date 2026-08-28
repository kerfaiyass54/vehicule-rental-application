import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  Output,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import {ClientBuyingService} from '../../../../services/client-services/client-buying.service';
import {SupplierInfo} from '../../../models/supplier-info.model';
import {VehiculeSearchDTO} from '../../../models/vehicule-search.model';
import {Buying} from '../../../models/buying.model';


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
export class BuyReview {

  private readonly buyingService =
    inject(ClientBuyingService);

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

  @Input()
  clientEmail = '';


  // =========================================================
  // OUTPUTS
  // =========================================================

  @Output()
  confirmed = new EventEmitter<Buying>();


  // =========================================================
  // STATE
  // =========================================================

  calculating = false;

  confirming = false;

  error = '';

  calculatedPrice: number | null = null;

  buying: Buying | null = null;


  // =========================================================
  // CALCULATE PRICE
  // =========================================================

  calculatePrice(): void {

    if (!this.vehicule) {
      this.error = 'No vehicle has been selected.';
      return;
    }

    if (!this.vehicule.idVehicule) {
      this.error = 'Vehicle ID is missing.';
      return;
    }

    if (!this.period || this.period < 1) {
      this.error = 'The rental period must be at least 1 day.';
      return;
    }

    this.error = '';

    /*
     * The vehicle price is assumed to be the price
     * for one rental period/day.
     *
     * Therefore:
     *
     * final price = vehicle price × period
     */

    this.calculatedPrice =
      this.vehicule.price * this.period;

    this.cdr.markForCheck();
  }


  // =========================================================
  // CONFIRM
  // =========================================================

  confirmPurchase(): void {

    if (!this.vehicule) {
      this.error = 'No vehicle has been selected.';
      return;
    }

    if (!this.vehicule.idVehicule) {
      this.error = 'Vehicle ID is missing.';
      return;
    }

    if (!this.clientEmail) {
      this.error = 'Client email is missing.';
      return;
    }

    if (!this.period || this.period < 1) {
      this.error = 'Invalid rental period.';
      return;
    }

    this.error = '';

    /*
     * Calculate price first if it has not
     * already been calculated.
     */

    if (this.calculatedPrice === null) {
      this.calculatePrice();
    }

    this.confirming = true;

    this.buyingService
      .addBuying(
        this.vehicule.idVehicule,
        this.clientEmail,
        this.period,
        this.renew
      )
      .pipe(
        finalize(() => {

          this.confirming = false;

          this.cdr.markForCheck();

        })
      )
      .subscribe({

        next: buying => {

          this.buying = buying;

          this.confirmed.emit(buying);

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to confirm vehicle purchase',
            error
          );

          this.error =
            error?.error?.message ??
            'Unable to complete the purchase. Please try again.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // PRICE FORMAT
  // =========================================================

  formatPrice(
    price: number | null
  ): string {

    if (price === null) {
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
  // VEHICLE PRICE
  // =========================================================

  get vehicleTotal(): number {

    if (!this.vehicule) {
      return 0;
    }

    return this.vehicule.price * this.period;

  }

}
