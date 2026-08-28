import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  output,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatTableModule
} from '@angular/material/table';

import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  MatChipsModule
} from '@angular/material/chips';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import Keycloak from 'keycloak-js';

import {
  ClientSubscriptionService
} from '../../../../services/client-services/client-subscription.service';

import {
  SupplierInfo
} from '../../../models/supplier-info.model';


// =========================================================
// SELECTED SUPPLIER
// =========================================================

export interface SelectedSupplier {

  supplier: SupplierInfo;

  subscribed: boolean;

}


// =========================================================
// COMPONENT
// =========================================================

@Component({

  selector: 'app-search-supplier',

  standalone: true,

  imports: [

    CommonModule,

    MatButtonModule,

    MatIconModule,

    MatTableModule,

    MatPaginatorModule,

    MatProgressSpinnerModule,

    MatTooltipModule,

    MatChipsModule

  ],

  templateUrl: './search-supplier.html',

  styleUrl: './search-supplier.css',

  changeDetection: ChangeDetectionStrategy.OnPush

})
export class SearchSupplier implements OnInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly subscriptionService =
    inject(ClientSubscriptionService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // OUTPUT
  // =========================================================

  readonly supplierSelected =
    output<SelectedSupplier>();


  // =========================================================
  // STATE
  // =========================================================

  readonly subscribedSuppliers =
    signal<SupplierInfo[]>([]);

  readonly unsubscribedSuppliers =
    signal<SupplierInfo[]>([]);

  readonly selectedSupplier =
    signal<SelectedSupplier | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly clientEmail =
    signal<string>('');


  // =========================================================
  // SEARCH
  // =========================================================

  readonly subscribedSearch =
    signal('');

  readonly unsubscribedSearch =
    signal('');


  // =========================================================
  // PAGINATION
  // =========================================================

  readonly subscribedPage =
    signal(0);

  readonly unsubscribedPage =
    signal(0);

  readonly subscribedPageSize =
    signal(5);

  readonly unsubscribedPageSize =
    signal(5);


  // =========================================================
  // TABLE COLUMNS
  // =========================================================

  readonly displayedColumns: string[] = [

    'supplier',

    'nationality',

    'vehicles',

    'subscriptions',

    'buyings',

    'action'

  ];


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
  // LOAD CLIENT EMAIL
  // =========================================================

  private loadClientEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'];

    if (
      !email ||
      typeof email !== 'string'
    ) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.loading.set(false);

      this.error.set(true);

      this.cdr.markForCheck();

      return;

    }

    this.clientEmail.set(email);

    this.loadSuppliers();

  }


  // =========================================================
  // LOAD SUPPLIERS
  // =========================================================

  private loadSuppliers(): void {

    const email =
      this.clientEmail();

    if (!email) {

      return;

    }

    this.loading.set(true);

    this.error.set(false);


    // -------------------------------------------------------
    // SUBSCRIBED SUPPLIERS
    // -------------------------------------------------------

    this.subscriptionService
      .getSubscribedSuppliers(email)

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: suppliers => {

          this.subscribedSuppliers.set(
            suppliers ?? []
          );

          this.loadUnsubscribedSuppliers();

        },

        error: error => {

          console.error(
            'Unable to load subscribed suppliers:',
            error
          );

          this.loading.set(false);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // LOAD UNSUBSCRIBED SUPPLIERS
  // =========================================================

  private loadUnsubscribedSuppliers(): void {

    const email =
      this.clientEmail();

    if (!email) {

      return;

    }

    this.subscriptionService

      .getUnsubscribedSuppliers(email)

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: suppliers => {

          this.unsubscribedSuppliers.set(
            suppliers ?? []
          );

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load unsubscribed suppliers:',
            error
          );

          this.loading.set(false);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // FILTERED SUBSCRIBED SUPPLIERS
  // =========================================================

  getFilteredSubscribedSuppliers(): SupplierInfo[] {

    const search =
      this.subscribedSearch()
        .trim()
        .toLowerCase();

    const suppliers =
      this.subscribedSuppliers();

    if (!search) {

      return suppliers;

    }

    return suppliers.filter(
      supplier =>

        supplier.suppName
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.nationality
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.email
          ?.toLowerCase()
          .includes(search)

    );

  }


  // =========================================================
  // FILTERED UNSUBSCRIBED SUPPLIERS
  // =========================================================

  getFilteredUnsubscribedSuppliers(): SupplierInfo[] {

    const search =
      this.unsubscribedSearch()
        .trim()
        .toLowerCase();

    const suppliers =
      this.unsubscribedSuppliers();

    if (!search) {

      return suppliers;

    }

    return suppliers.filter(
      supplier =>

        supplier.suppName
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.nationality
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.email
          ?.toLowerCase()
          .includes(search)

    );

  }


  // =========================================================
  // PAGINATED SUBSCRIBED SUPPLIERS
  // =========================================================

  get paginatedSubscribedSuppliers():
    SupplierInfo[] {

    const suppliers =
      this.getFilteredSubscribedSuppliers();

    const start =
      this.subscribedPage() *
      this.subscribedPageSize();

    return suppliers.slice(

      start,

      start + this.subscribedPageSize()

    );

  }


  // =========================================================
  // PAGINATED UNSUBSCRIBED SUPPLIERS
  // =========================================================

  get paginatedUnsubscribedSuppliers():
    SupplierInfo[] {

    const suppliers =
      this.getFilteredUnsubscribedSuppliers();

    const start =
      this.unsubscribedPage() *
      this.unsubscribedPageSize();

    return suppliers.slice(

      start,

      start + this.unsubscribedPageSize()

    );

  }


  // =========================================================
  // SUBSCRIBED PAGINATION
  // =========================================================

  onSubscribedPageChange(
    event: PageEvent
  ): void {

    this.subscribedPage.set(
      event.pageIndex
    );

    this.subscribedPageSize.set(
      event.pageSize
    );

  }


  // =========================================================
  // UNSUBSCRIBED PAGINATION
  // =========================================================

  onUnsubscribedPageChange(
    event: PageEvent
  ): void {

    this.unsubscribedPage.set(
      event.pageIndex
    );

    this.unsubscribedPageSize.set(
      event.pageSize
    );

  }


  // =========================================================
  // SEARCH SUBSCRIBED
  // =========================================================

  searchSubscribed(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    this.subscribedSearch.set(
      input.value
    );

    this.subscribedPage.set(0);

  }


  // =========================================================
  // SEARCH UNSUBSCRIBED
  // =========================================================

  searchUnsubscribed(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    this.unsubscribedSearch.set(
      input.value
    );

    this.unsubscribedPage.set(0);

  }


  // =========================================================
  // SELECT SUPPLIER
  // =========================================================

  selectSupplier(
    supplier: SupplierInfo,
    subscribed: boolean
  ): void {

    const selection: SelectedSupplier = {

      supplier,

      subscribed

    };

    this.selectedSupplier.set(
      selection
    );

    this.supplierSelected.emit(
      selection
    );

    this.cdr.markForCheck();

  }


  // =========================================================
  // CHECK SELECTION
  // =========================================================

  isSelected(
    supplier: SupplierInfo
  ): boolean {

    return (

      this.selectedSupplier()
        ?.supplier
        .idSupp === supplier.idSupp

    );

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {

      return;

    }

    this.loadSuppliers();

  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    this.error.set(false);

    this.loadClientEmail();

  }


  // =========================================================
  // SUPPLIER INITIALS
  // =========================================================

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
        part =>
          part
            .charAt(0)
            .toUpperCase()
      )

      .join('');

  }


  // =========================================================
  // TRACK BY
  // =========================================================

  trackSupplier(
    _: number,
    supplier: SupplierInfo
  ): number {

    return supplier.idSupp;

  }

}
