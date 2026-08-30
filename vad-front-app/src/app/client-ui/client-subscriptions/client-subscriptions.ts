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

import { Subject, finalize, takeUntil } from 'rxjs';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { ClientSubscriptionService } from '../../services/client-services/client-subscription.service';

import { SubscriptionInfo } from '../models/subscription-info.model';
import { SubscriptionType } from '../enums/subscription-type';


interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}


@Component({
  selector: 'app-client-subscriptions',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatInputModule
  ],

  templateUrl: './client-subscriptions.html',
  styleUrl: './client-subscriptions.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientSubscriptions implements OnInit, OnDestroy {

  private readonly keycloak =
    inject(Keycloak);

  private readonly subscriptionService =
    inject(ClientSubscriptionService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly subscriptions =
    signal<SubscriptionInfo[]>([]);

  readonly filteredSubscriptions =
    signal<SubscriptionInfo[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly clientEmail =
    signal('');

  readonly searchTerm =
    signal('');

  readonly page =
    signal(0);

  readonly size =
    signal(10);

  readonly totalElements =
    signal(0);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadClientEmail();

  }


  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadClientEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.clientEmail.set(email);

    this.loadSubscriptions();

  }


  // =========================================================
  // LOAD SUBSCRIPTIONS
  // =========================================================

  loadSubscriptions(): void {

    const email =
      this.clientEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();


    this.subscriptionService
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

          const content =
            response.content ?? [];

          this.subscriptions.set(content);

          this.filteredSubscriptions.set(
            this.applySearch(
              content,
              this.searchTerm()
            )
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.error.set(false);

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to load client subscriptions',
            error
          );

          this.subscriptions.set([]);
          this.filteredSubscriptions.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    const value =
      input.value
        .trim()
        .toLowerCase();

    this.searchTerm.set(value);

    this.filteredSubscriptions.set(
      this.applySearch(
        this.subscriptions(),
        value
      )
    );

  }


  private applySearch(
    subscriptions: SubscriptionInfo[],
    search: string
  ): SubscriptionInfo[] {

    if (!search) {
      return subscriptions;
    }

    return subscriptions.filter(
      subscription => {

        return (

          String(subscription.idSubscrip)
            .toLowerCase()
            .includes(search)

          ||

          String(subscription.type)
            .toLowerCase()
            .includes(search)

          ||

          String(subscription.idSupplier)
            .toLowerCase()
            .includes(search)

          ||

          String(subscription.clientEmail)
            .toLowerCase()
            .includes(search)

          ||

          String(subscription.price)
            .toLowerCase()
            .includes(search)

        );

      }
    );

  }


  clearSearch(): void {

    this.searchTerm.set('');

    this.filteredSubscriptions.set(
      this.subscriptions()
    );

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadSubscriptions();

  }


  // =========================================================
  // PAGINATION
  // =========================================================

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


  // =========================================================
  // TRACK
  // =========================================================

  trackSubscription(
    _index: number,
    subscription: SubscriptionInfo
  ): number {

    return subscription.idSubscrip;

  }


  // =========================================================
  // TYPE
  // =========================================================

  getTypeClass(
    type: SubscriptionType
  ): string {

    switch (type) {

      case SubscriptionType.BASIC:
        return 'type-basic';

      case SubscriptionType.PREMIUM:
        return 'type-premium';

      case SubscriptionType.MONTHLY:
        return 'type-monthly';

      case SubscriptionType.ANNUAL:
        return 'type-annual';

      default:
        return 'type-default';

    }

  }


  getTypeIcon(
    type: SubscriptionType
  ): string {

    switch (type) {

      case SubscriptionType.BASIC:
        return 'workspace_premium';

      case SubscriptionType.PREMIUM:
        return 'diamond';

      case SubscriptionType.MONTHLY:
        return 'calendar_month';

      case SubscriptionType.ANNUAL:
        return 'event_available';

      default:
        return 'subscriptions';

    }

  }


  // =========================================================
  // DATE
  // =========================================================

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
      return '—';
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


  // =========================================================
  // PRICE
  // =========================================================

  formatPrice(
    price: number
  ): string {

    return new Intl.NumberFormat(
      'en-US',
      {
        style: 'currency',
        currency: 'EUR'
      }
    ).format(price ?? 0);

  }


  // =========================================================
  // DISCOUNT
  // =========================================================

  getDiscountLabel(
    reduction: number
  ): string {

    if (!reduction || reduction <= 0) {
      return 'No discount';
    }

    return `${reduction}% OFF`;

  }

}
