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

import { CommonModule } from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  SubscriptionType
} from '../../../enums/subscription-type';

import {
  SupplierInfo
} from '../../../models/supplier-info.model';

import {
  SubscriptionInfo
} from '../../../models/subscription-info.model';
import {MatIcon} from '@angular/material/icon';


@Component({
  selector: 'app-subscription-review',

  standalone: true,

  imports: [
    CommonModule,
    MatIcon
  ],

  templateUrl: './subscription-review.html',

  styleUrl: './subscription-review.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SubscriptionReview implements OnChanges {


  // =========================================================
  // SERVICES
  // =========================================================

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
  subscriptionType: SubscriptionType | null = null;


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  subscriptionReady =
    new EventEmitter<SubscriptionInfo>();


  // =========================================================
  // STATE
  // =========================================================

  clientEmail = '';

  dateStart: Date = new Date();

  price = 0;

  reduce = 0;


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    if (
      changes['supplier'] ||
      changes['subscriptionType']
    ) {

      this.loadReview();

    }

  }


  // =========================================================
  // LOAD REVIEW
  // =========================================================

  private loadReview(): void {

    // ---------------------------------------------------------
    // RESET
    // ---------------------------------------------------------

    this.price = 0;

    this.reduce = 0;


    // ---------------------------------------------------------
    // CLIENT EMAIL
    // ---------------------------------------------------------

    this.loadClientEmail();


    // ---------------------------------------------------------
    // START DATE
    // ---------------------------------------------------------

    this.dateStart =
      new Date();


    // ---------------------------------------------------------
    // SUBSCRIPTION TYPE
    // ---------------------------------------------------------

    if (!this.subscriptionType) {

      this.cdr.markForCheck();

      return;

    }


    // ---------------------------------------------------------
    // GET SUBSCRIPTION VALUES
    // ---------------------------------------------------------

    switch (this.subscriptionType) {

      case SubscriptionType.BASIC:

        this.price = 99;
        this.reduce = 5;

        break;


      case SubscriptionType.PREMIUM:

        this.price = 199;
        this.reduce = 15;

        break;


      case SubscriptionType.MONTHLY:

        this.price = 39;
        this.reduce = 10;

        break;


      case SubscriptionType.ANNUAL:

        this.price = 399;
        this.reduce = 25;

        break;

    }


    this.cdr.markForCheck();

  }


  // =========================================================
  // GET CLIENT EMAIL
  // =========================================================

  private loadClientEmail(): void {

    const token =
      this.keycloak.tokenParsed;


    const email =
      token?.['email'] as string | undefined;


    if (
      !email ||
      !email.trim()
    ) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.clientEmail = '';

      return;

    }


    this.clientEmail =
      email.trim();

  }


  // =========================================================
  // GET SUBSCRIPTION NAME
  // =========================================================

  get subscriptionName(): string {

    if (!this.subscriptionType) {

      return '—';

    }


    return this.subscriptionType;

  }


  // =========================================================
  // GET SUPPLIER NAME
  // =========================================================

  get supplierName(): string {

    return this.supplier?.suppName ??
      '—';

  }


  // =========================================================
  // BUILD SUBSCRIPTION
  // =========================================================

  buildSubscription(): SubscriptionInfo | null {

    if (
      !this.subscriptionType ||
      !this.supplier ||
      !this.clientEmail
    ) {

      return null;

    }


    return {

      type: this.subscriptionType,

      dateStart:
        this.dateStart.toISOString(),

      reduce: this.reduce,

      price: this.price,

      idSupplier:
      this.supplier.idSupp,

      clientEmail:
      this.clientEmail

    } as SubscriptionInfo;

  }


  // =========================================================
  // CONFIRM / CONTINUE
  // =========================================================

  confirm(): void {

    const subscription =
      this.buildSubscription();


    if (!subscription) {

      return;

    }


    this.subscriptionReady.emit(
      subscription
    );

  }


  // =========================================================
  // FORMAT PRICE
  // =========================================================

  formatPrice(
    price: number
  ): string {

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
  // FORMAT DATE
  // =========================================================

  formatDate(
    date: Date
  ): string {

    return new Intl.DateTimeFormat(
      'en-US',
      {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
      }
    ).format(date);

  }

}
