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
import {
  MatDialog,
  MatDialogModule
} from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { SupplierAddresses } from '../../services/supplier-services/supplier-addresses';
import { SupplierAddressResponse } from '../models/supplier-address-response.model';
import { AddSupplierAddress } from './add-supplier-address/add-supplier-address';


@Component({
  selector: 'app-supplier-adresses',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatTooltipModule
  ],

  templateUrl: './supplier-adresses.html',
  styleUrl: './supplier-adresses.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierAdresses implements OnInit, OnDestroy {

  // ---------------------------------------------------------
  // DEPENDENCIES
  // ---------------------------------------------------------

  private readonly keycloak = inject(Keycloak);

  private readonly supplierAddressesService =
    inject(SupplierAddresses);

  private readonly dialog =
    inject(MatDialog);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // ---------------------------------------------------------
  // STATE
  // ---------------------------------------------------------

  readonly addresses =
    signal<SupplierAddressResponse[]>([]);

  readonly loading =
    signal(true);

  readonly freeing =
    signal<number | null>(null);

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
  // ADD ADDRESS
  // ---------------------------------------------------------

  addAddress(): void {

    const dialogRef =
      this.dialog.open(AddSupplierAddress, {

        width: '1200px',

        maxWidth: '95vw',

        maxHeight: '110vh',

        autoFocus: false,

        panelClass: 'add-address-dialog'
      });


    dialogRef.afterClosed()
      .pipe(
        takeUntil(this.destroy$)
      )
      .subscribe((created: boolean) => {

        if (created) {
          this.page.set(0);
          this.loadAddresses();
        }


        if (!created) {
          return;
        }

        /*
         * After creating an address,
         * return to the first page and reload.
         */
        this.page.set(0);

        this.loadAddresses();
      });
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

    this.loadAddresses();
  }


  // ---------------------------------------------------------
  // LOAD ADDRESSES
  // ---------------------------------------------------------

  loadAddresses(): void {

    const email =
      this.supplierEmail();


    if (!email) {
      return;
    }


    this.loading.set(true);

    this.error.set(false);

    this.cdr.markForCheck();


    this.supplierAddressesService
      .getSupplierAddresses(
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

          this.addresses.set(
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
            'Unable to load supplier addresses',
            error
          );

          this.addresses.set([]);

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

    this.loadAddresses();
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

    this.loadAddresses();
  }


  // ---------------------------------------------------------
  // FREE ADDRESS
  // ---------------------------------------------------------

  freeAddress(
    address: SupplierAddressResponse
  ): void {

    const addressLabel =
      `${address.number} ${address.road}, ${address.location}`;


    const confirmed =
      window.confirm(
        `Free this address?\n\n` +
        `${addressLabel}\n\n` +
        `This will remove the supplier association.`
      );


    if (!confirmed) {
      return;
    }


    this.freeAddressConfirmed(address);
  }


  private freeAddressConfirmed(
    address: SupplierAddressResponse
  ): void {

    this.freeing.set(
      address.idAddress
    );

    this.cdr.markForCheck();


    this.supplierAddressesService
      .freeAddress(
        address.idAddress
      )
      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.freeing.set(null);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: () => {

          /*
           * If the current page contained
           * only one address, go back.
           */
          if (
            this.addresses().length === 1 &&
            this.page() > 0
          ) {

            this.page.update(
              current => current - 1
            );
          }


          /*
           * Reload addresses.
           */
          this.loadAddresses();
        },


        error: error => {

          console.error(
            'Unable to free address',
            error
          );


          window.alert(
            'Unable to free this address. Please try again.'
          );

          this.cdr.markForCheck();
        }
      });
  }


  // ---------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------

  trackAddress(
    _index: number,
    address: SupplierAddressResponse
  ): number {

    return address.idAddress;
  }


  getAddressNumber(
    address: SupplierAddressResponse
  ): string {

    return address.number
      ? `${address.number}`
      : '—';
  }
}

