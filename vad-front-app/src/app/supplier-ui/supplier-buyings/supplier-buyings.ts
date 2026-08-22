
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

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { SupplierBuying } from '../../services/supplier-services/supplier-buyings';
import {BuyingResponse} from '../models/buying-response.model';






@Component({
  selector: 'app-supplier-buyings',
  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],

  templateUrl: './supplier-buyings.html',
  styleUrl: './supplier-buyings.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierBuyingsComponent
  implements OnInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly supplierBuyingsService =
    inject(SupplierBuying);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly buyings =
    signal<BuyingResponse[]>([]);

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

  readonly totalPages =
    signal(0);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadSupplierEmail();
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  // =========================================================
  // LOAD SUPPLIER EMAIL
  // =========================================================

  private loadSupplierEmail(): void {

    const token = this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;

    if (!email) {

      console.error(
        'Supplier email was not found in Keycloak token.'
      );

      this.loading.set(false);
      this.error.set(true);

      this.cdr.markForCheck();

      return;
    }

    this.supplierEmail.set(email);

    this.loadBuyings();
  }


  // =========================================================
  // LOAD BUYINGS
  // =========================================================

  loadBuyings(): void {

    const email = this.supplierEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();


    this.supplierBuyingsService
      .getBuyings(
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

          this.buyings.set(
            response.content ?? []
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.totalPages.set(
            response.totalPages ?? 0
          );

          this.error.set(false);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load supplier buyings:',
            error
          );

          this.buyings.set([]);

          this.totalElements.set(0);

          this.totalPages.set(0);

          this.error.set(true);

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadBuyings();
  }


  // =========================================================
  // PAGINATION
  // =========================================================

  nextPage(): void {

    if (
      this.page() >= this.totalPages() - 1 ||
      this.loading()
    ) {
      return;
    }

    this.page.update(
      current => current + 1
    );

    this.loadBuyings();
  }


  previousPage(): void {

    if (
      this.page() <= 0 ||
      this.loading()
    ) {
      return;
    }

    this.page.update(
      current => current - 1
    );

    this.loadBuyings();
  }


  goToPage(page: number): void {

    if (
      page < 0 ||
      page >= this.totalPages() ||
      page === this.page() ||
      this.loading()
    ) {
      return;
    }

    this.page.set(page);

    this.loadBuyings();
  }


  // =========================================================
  // HELPERS
  // =========================================================

  getInitials(name: string): string {

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


  formatDate(date: string): string {

    if (!date) {
      return '—';
    }

    const value = new Date(date);

    if (Number.isNaN(value.getTime())) {
      return '—';
    }

    return value.toLocaleDateString(
      'en-GB',
      {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      }
    );
  }


  formatTime(date: string): string {

    if (!date) {
      return '';
    }

    const value = new Date(date);

    if (Number.isNaN(value.getTime())) {
      return '';
    }

    return value.toLocaleTimeString(
      'en-GB',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    );
  }


  trackBuying(
    _index: number,
    buying: BuyingResponse
  ): number {

    return buying.idBuying;
  }


  getPageNumbers(): number[] {

    const pages = this.totalPages();

    if (pages <= 0) {
      return [];
    }

    const result: number[] = [];

    for (let i = 0; i < pages; i++) {

      if (
        i < 5 ||
        i === pages - 1 ||
        Math.abs(i - this.page()) <= 1
      ) {
        result.push(i);
      }
    }

    return [...new Set(result)];
  }

}
