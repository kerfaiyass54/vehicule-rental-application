import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
  Output,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  catchError,
  finalize,
  of
} from 'rxjs';

import Keycloak from 'keycloak-js';

import {
  ClientSubscriptionService
} from '../../../../services/client-services/client-subscription.service';

import {
  SupplierInfo
} from '../../../models/supplier-info.model';

import { MatIcon } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-search-supplier',

  standalone: true,

  imports: [
    CommonModule,
    MatIcon,
    MatProgressSpinner,
    FormsModule
  ],

  templateUrl: './search-supplier.html',

  styleUrl: './search-supplier.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SearchSupplier implements OnInit {


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
  // OUTPUT
  // =========================================================

  @Output()
  supplierSelected =
    new EventEmitter<SupplierInfo>();


  // =========================================================
  // STATE
  // =========================================================

  suppliers: SupplierInfo[] = [];

  selectedSupplier: SupplierInfo | null = null;

  clientEmail = '';

  loading =
    signal(false);

  error = false;

  searchTerm = '';


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadLoggedInClient();

  }


  // =========================================================
  // GET LOGGED-IN CLIENT
  // =========================================================

  private loadLoggedInClient(): void {

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

      this.error = true;

      this.cdr.markForCheck();

      return;

    }


    this.clientEmail =
      email.trim();


    this.loadUnsubscribedSuppliers();

  }


  // =========================================================
  // LOAD UNSUBSCRIBED SUPPLIERS
  // =========================================================

  private loadUnsubscribedSuppliers(): void {

    this.loading.set(true);

    this.error = false;

    this.cdr.markForCheck();


    this.subscriptionService
      .getUnsubscribedSuppliers(
        this.clientEmail
      )
      .pipe(

        catchError(error => {

          console.error(
            'Unable to retrieve unsubscribed suppliers:',
            error
          );

          this.error = true;

          return of([]);

        }),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )
      .subscribe({

        next: suppliers => {

          this.suppliers =
            suppliers;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SEARCH
  // =========================================================

  get filteredSuppliers(): SupplierInfo[] {

    const search =
      this.searchTerm
        .trim()
        .toLowerCase();


    if (!search) {

      return this.suppliers;

    }


    return this.suppliers.filter(
      supplier => {

        const name =
          supplier.suppName
            ?.toLowerCase() ?? '';

        const email =
          supplier.email
            ?.toLowerCase() ?? '';


        return (
          name.includes(search) ||
          email.includes(search)
        );

      }
    );

  }


  // =========================================================
  // SELECT SUPPLIER
  // =========================================================

  selectSupplier(
    supplier: SupplierInfo
  ): void {

    this.selectedSupplier =
      supplier;


    this.supplierSelected.emit(
      supplier
    );


    this.cdr.markForCheck();

  }


  // =========================================================
  // CHECK SELECTION
  // =========================================================

  isSelected(
    supplier: SupplierInfo
  ): boolean {

    if (!this.selectedSupplier) {

      return false;

    }


    return (
      this.selectedSupplier.email ===
      supplier.email
    );

  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    if (!this.clientEmail) {

      this.loadLoggedInClient();

      return;

    }


    this.loadUnsubscribedSuppliers();

  }

}
