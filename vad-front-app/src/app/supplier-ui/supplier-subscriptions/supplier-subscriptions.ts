import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';
import Keycloak from 'keycloak-js';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  SupplierSubscriptionsService
} from '../../services/supplier-services/supplier-subscriptions';
import {SubscriptionResponse} from '../models/subscription-response.model';
import {SubscriptionType} from '../models/subscription-type.enum';


// ---------------------------------------------------------
// MODEL
// ---------------------------------------------------------




// ---------------------------------------------------------
// COMPONENT
// ---------------------------------------------------------

@Component({
  selector: 'app-supplier-subscriptions',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './supplier-subscriptions.html',
  styleUrl: './supplier-subscriptions.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierSubscriptions
  implements OnInit, OnDestroy {


  // ---------------------------------------------------------
  // DEPENDENCIES
  // ---------------------------------------------------------

  private readonly keycloak =
    inject(Keycloak);

  private readonly subscriptionsService =
    inject(SupplierSubscriptionsService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // ---------------------------------------------------------
  // STATE
  // ---------------------------------------------------------

  readonly subscriptions =
    signal<SubscriptionResponse[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly supplierEmail =
    signal('');

  readonly page =
    signal(0);

  readonly size =
    signal(6);

  readonly totalElements =
    signal(0);


  // ---------------------------------------------------------
  // LIFECYCLE
  // ---------------------------------------------------------

  ngOnInit(): void {
    this.loadSupplierEmail();
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  // ---------------------------------------------------------
  // KEYCLOAK
  // ---------------------------------------------------------

  private loadSupplierEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    if (!email) {

      console.error(
        'Supplier email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.supplierEmail.set(email);

    this.loadSubscriptions();
  }


  // ---------------------------------------------------------
  // LOAD SUBSCRIPTIONS
  // ---------------------------------------------------------

  loadSubscriptions(): void {

    const email =
      this.supplierEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();

    this.subscriptionsService
      .getSubscriptions(
        email,
        this.page(),
        this.size()
      )
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: response => {

          this.subscriptions.set(
            response.content ?? []
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.error.set(false);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load supplier subscriptions',
            error
          );

          this.subscriptions.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // ---------------------------------------------------------
  // REFRESH
  // ---------------------------------------------------------

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadSubscriptions();
  }


  // ---------------------------------------------------------
  // PAGINATION
  // ---------------------------------------------------------

  onPageChange(
    event: PageEvent
  ): void {

    this.page.set(
      event.pageIndex
    );

    this.size.set(
      event.pageSize
    );

    this.loadSubscriptions();
  }


  // ---------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------

  trackSubscription(
    _index: number,
    subscription: SubscriptionResponse
  ): number {

    return subscription.idSubscription;
  }


  getInitials(
    name: string
  ): string {

    if (!name) {
      return '?';
    }

    return name
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map(
        part => part.charAt(0).toUpperCase()
      )
      .join('');
  }


  formatDate(
    date: string
  ): string {

    if (!date) {
      return '—';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      }
    ).format(
      new Date(date)
    );
  }


  formatTime(
    date: string
  ): string {

    if (!date) {
      return '';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ).format(
      new Date(date)
    );
  }


  getSubscriptionIcon(
    type: SubscriptionType
  ): string {

    switch (type) {

      case SubscriptionType.PREMIUM:
        return 'workspace_premium';

      case SubscriptionType.BASIC:
        return 'star_border';

      default:
        return 'subscriptions';
    }
  }


  getSubscriptionLabel(
    type: SubscriptionType
  ): string {

    if (!type) {
      return 'Unknown';
    }

    return type.charAt(0) +
      type.slice(1).toLowerCase();
  }


  getTypeClass(
    type: SubscriptionType
  ): string {

    return `subscription-${type
      ?.toString()
      .toLowerCase()}`;
  }


  getReductionLabel(
    reduce: number
  ): string {

    if (!reduce || reduce <= 0) {
      return 'No reduction';
    }

    return `${reduce}% reduction`;
  }


  getPriceLabel(
    price: number
  ): string {

    if (price === null || price === undefined) {
      return '€0.00';
    }

    return new Intl.NumberFormat(
      'en-GB',
      {
        style: 'currency',
        currency: 'EUR'
      }
    ).format(price);
  }


  // ---------------------------------------------------------
  // STATISTICS
  // ---------------------------------------------------------

  getTotalPrice(): number {

    return this.subscriptions()
      .reduce(
        (total, subscription) =>
          total + (subscription.price || 0),
        0
      );
  }


  getAveragePrice(): number {

    const list =
      this.subscriptions();

    if (!list.length) {
      return 0;
    }

    return this.getTotalPrice() /
      list.length;
  }


  getTotalReductions(): number {

    return this.subscriptions()
      .reduce(
        (total, subscription) =>
          total + (subscription.reduce || 0),
        0
      );
  }
}
