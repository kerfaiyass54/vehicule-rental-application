import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  finalize
} from 'rxjs';

import Keycloak from 'keycloak-js';



import {
  RouterLink
} from '@angular/router';
import {ClientSubscriptionService} from '../../services/client-services/client-subscription.service';
import {SubscriptionInfo} from '../models/subscription-info.model';
import {MatProgressSpinner} from '@angular/material/progress-spinner';


@Component({
  selector: 'app-client-subscriptions',

  standalone: true,

  imports: [

    CommonModule,

    MatIconModule,

    MatButtonModule,

    MatFormFieldModule,

    MatInputModule,

    MatPaginatorModule,

    MatTooltipModule,

    RouterLink,
    MatProgressSpinner

  ],

  templateUrl: './client-subscriptions.html',

  styleUrl: './client-subscriptions.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientSubscriptions implements OnInit {


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
  // STATE
  // =========================================================

  subscriptions =
    signal<SubscriptionInfo[]>([]);


  filteredSubscriptions =
    signal<SubscriptionInfo[]>([]);


  loading =
    signal(false);


  error =
    signal(false);


  searchTerm =
    signal('');


  totalElements =
    signal(0);


  page =
    signal(0);


  size =
    signal(5);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadSubscriptions();

  }


  // =========================================================
  // LOAD SUBSCRIPTIONS
  // =========================================================

  loadSubscriptions(): void {

    const email =
      this.getClientEmail();


    if (!email) {

      this.error.set(true);

      this.cdr.markForCheck();

      return;

    }


    this.loading.set(true);

    this.error.set(false);


    this.subscriptionService
      .getSubscriptions(
        email,
        this.page(),
        this.size()
      )
      .pipe(

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )
      .subscribe({

        next: response => {

          const content =
            response?.content ?? [];


          this.subscriptions.set(
            content
          );


          this.totalElements.set(
            response?.totalElements ?? content.length
          );


          this.applySearch();


          this.cdr.markForCheck();

        },


        error: err => {

          console.error(
            'Unable to load subscriptions:',
            err
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
  // GET CLIENT EMAIL
  // =========================================================

  private getClientEmail(): string {

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

      return '';

    }


    return email.trim();

  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;


    this.searchTerm.set(
      input.value
    );


    this.page.set(0);


    this.applySearch();

  }


  // =========================================================
  // APPLY SEARCH
  // =========================================================

  private applySearch(): void {

    const search =
      this.searchTerm()
        .trim()
        .toLowerCase();


    if (!search) {

      this.filteredSubscriptions.set(
        this.subscriptions()
      );

      return;

    }


    const filtered =
      this.subscriptions().filter(
        subscription => {

          const type =
            subscription.type
              ?.toString()
              .toLowerCase() ?? '';


          const supplier =
            subscription.idSupplier
              ?.toString()
              .toLowerCase() ?? '';


          const id =
            subscription.idSubscrip
              ?.toString()
              .toLowerCase() ?? '';


          return (
            type.includes(search) ||
            supplier.includes(search) ||
            id.includes(search)
          );

        }
      );


    this.filteredSubscriptions.set(
      filtered
    );

  }


  // =========================================================
  // CLEAR SEARCH
  // =========================================================

  clearSearch(): void {

    this.searchTerm.set('');

    this.page.set(0);

    this.applySearch();

    this.cdr.markForCheck();

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
    index: number,
    subscription: SubscriptionInfo
  ): number | string {

    return subscription.idSubscrip ?? index;

  }


  // =========================================================
  // TYPE CLASS
  // =========================================================

  getTypeClass(
    type: any
  ): string {

    if (!type) {

      return 'type-default';

    }


    switch (
      type.toString().toLowerCase()
      ) {

      case 'basic':

        return 'type-basic';


      case 'premium':

        return 'type-premium';


      case 'monthly':

        return 'type-monthly';


      case 'annual':

        return 'type-annual';


      default:

        return 'type-default';

    }

  }


  // =========================================================
  // TYPE ICON
  // =========================================================

  getTypeIcon(
    type: any
  ): string {

    if (!type) {

      return 'card_membership';

    }


    switch (
      type.toString().toLowerCase()
      ) {

      case 'basic':

        return 'card_membership';


      case 'premium':

        return 'workspace_premium';


      case 'monthly':

        return 'calendar_month';


      case 'annual':

        return 'event_available';


      default:

        return 'card_membership';

    }

  }


  // =========================================================
  // FORMAT DATE
  // =========================================================

  formatDate(
    date: any
  ): string {

    if (!date) {

      return '—';

    }


    const parsedDate =
      new Date(date);


    if (
      isNaN(
        parsedDate.getTime()
      )
    ) {

      return '—';

    }


    return new Intl.DateTimeFormat(
      'en-US',
      {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
      }
    ).format(parsedDate);

  }


  // =========================================================
  // FORMAT TIME
  // =========================================================

  formatTime(
    date: any
  ): string {

    if (!date) {

      return '';

    }


    const parsedDate =
      new Date(date);


    if (
      isNaN(
        parsedDate.getTime()
      )
    ) {

      return '';

    }


    return new Intl.DateTimeFormat(
      'en-US',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ).format(parsedDate);

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
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }
    ).format(
      price ?? 0
    );

  }


  // =========================================================
  // DISCOUNT LABEL
  // =========================================================

  getDiscountLabel(
    reduction: number
  ): string {

    if (!reduction || reduction <= 0) {

      return 'No discount';

    }


    return `${reduction}%`;

  }

}
