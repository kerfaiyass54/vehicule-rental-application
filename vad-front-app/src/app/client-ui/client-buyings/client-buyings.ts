import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { Router } from '@angular/router';

import Keycloak from 'keycloak-js';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTableModule } from '@angular/material/table';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { BuyingResponse } from '../../supplier-ui/models/buying-response.model';

import { ClientBuyingService } from '../../services/client-services/client-buying.service';


@Component({
  selector: 'app-client-buyings',
  standalone: true,

  imports: [
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],

  templateUrl: './client-buyings.html',
  styleUrl: './client-buyings.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientBuyings
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly buyingService =
    inject(ClientBuyingService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly buyings =
    signal<any[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly clientEmail =
    signal('');

  readonly page =
    signal(0);

  readonly size =
    signal(6);

  readonly totalElements =
    signal(0);


  // =========================================================
  // TABLE COLUMNS
  // =========================================================

  readonly displayedColumns = [

    'idBuying',

    'vehicle',

    'date',

    'period',

    'status',

    'actions'

  ];


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadClientEmail();

  }


  ngAfterViewInit(): void {

    this.setupRevealAnimation();

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
      token?.['email'] as string | undefined;


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

    this.loadBuyings();

  }


  // =========================================================
  // LOAD BUYINGS
  // =========================================================

  loadBuyings(): void {

    const email =
      this.clientEmail();


    if (!email) {

      return;

    }


    this.loading.set(true);

    this.error.set(false);

    this.cdr.markForCheck();


    this.buyingService

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


          setTimeout(() => {

            this.setupRevealAnimation();

          });

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


          this.error.set(false);

          this.cdr.markForCheck();

        },


        error: err => {

          console.error(
            'Unable to load client buyings',
            err
          );


          this.buyings.set([]);

          this.totalElements.set(0);

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

  onPageChange(
    event: PageEvent
  ): void {

    this.page.set(
      event.pageIndex
    );


    this.size.set(
      event.pageSize
    );


    this.loadBuyings();


    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================================
  // ADD BUYING
  // =========================================================

  addBuying(): void {

    this.router.navigate([
      '/client/buyings/buy-vehicule'
    ]);

  }


  // =========================================================
  // HELPERS
  // =========================================================

  trackBuying(
    _index: number,
    buying: BuyingResponse
  ): number {

    return buying.idBuying;

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
        part =>
          part
            .charAt(0)
            .toUpperCase()
      )

      .join('');

  }


  formatDate(
    date: string | Date
  ): string {

    if (!date) {

      return '—';

    }


    return new Intl.DateTimeFormat(
      'en-US',
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
    date: string | Date
  ): string {

    if (!date) {

      return '—';

    }


    return new Intl.DateTimeFormat(
      'en-US',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ).format(
      new Date(date)
    );

  }


  getStatusClass(
    status: string
  ): string {

    switch (
      status?.toUpperCase()
      ) {

      case 'CONFIRMED':
      case 'APPROVED':
      case 'ACTIVE':

        return 'status-success';


      case 'PENDING':

        return 'status-pending';


      case 'CANCELLED':
      case 'REFUSED':
      case 'REJECTED':

        return 'status-danger';


      default:

        return 'status-neutral';

    }

  }


  getStatusIcon(
    status: string
  ): string {

    switch (
      status?.toUpperCase()
      ) {

      case 'CONFIRMED':
      case 'APPROVED':
      case 'ACTIVE':

        return 'check_circle';


      case 'PENDING':

        return 'schedule';


      case 'CANCELLED':
      case 'REFUSED':
      case 'REJECTED':

        return 'cancel';


      default:

        return 'info';

    }

  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private setupRevealAnimation(): void {

    const elements =
      document.querySelectorAll('.reveal');


    if (!elements.length) {

      return;

    }


    const observer =
      new IntersectionObserver(

        entries => {

          entries.forEach(entry => {

            if (entry.isIntersecting) {

              entry.target.classList.add(
                'visible'
              );

            } else {

              entry.target.classList.remove(
                'visible'
              );

            }

          });

        },

        {
          threshold: 0.12
        }

      );


    elements.forEach(element => {

      observer.observe(element);

    });

  }

}
