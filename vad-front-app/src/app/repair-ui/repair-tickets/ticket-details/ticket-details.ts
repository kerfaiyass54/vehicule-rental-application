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
import { ActivatedRoute, Router } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  Subject,
  forkJoin,
  finalize,
  takeUntil
} from 'rxjs';
import {RepairTicketService} from '../../../services/repair-services/repair-ticket.service';
import {TicketClient} from '../../models/ticket-client.model';
import {TicketVehicule} from '../../models/ticket-vehicule.model';
import {TicketDetailsModel} from '../../models/ticket-details.model';




@Component({
  selector: 'app-ticket-details',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './ticket-details.html',
  styleUrl: './ticket-details.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketDetails implements OnInit, OnDestroy {

  // =========================================================
  // INJECTIONS
  // =========================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly ticketService =
    inject(RepairTicketService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly ticket =
    signal<TicketDetailsModel | null>(null);

  readonly client =
    signal<TicketClient | null>(null);

  readonly vehicule =
    signal<TicketVehicule | null>(null);

  readonly ticketId =
    signal<number | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadTicketId();

  }


  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

  }


  // =========================================================
  // LOAD TICKET ID
  // =========================================================

  private loadTicketId(): void {

    const id =
      Number(
        this.route.snapshot.paramMap.get('id')
      );

    if (!id || id <= 0) {

      console.error(
        'Invalid ticket ID.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.ticketId.set(id);

    this.loadDetails();

  }


  // =========================================================
  // LOAD DETAILS
  // =========================================================

  loadDetails(): void {

    const id =
      this.ticketId();

    if (!id) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.ticket.set(null);
    this.client.set(null);
    this.vehicule.set(null);

    this.cdr.markForCheck();


    forkJoin({

      ticket:
        this.ticketService.getTicketInfo(id),

      client:
        this.ticketService.getClient(id),

      vehicule:
        this.ticketService.getVehicule(id)

    })

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          this.ticket.set(
            response.ticket
          );

          this.client.set(
            response.client
          );

          this.vehicule.set(
            response.vehicule
          );

          this.error.set(false);

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load ticket details',
            error
          );

          this.ticket.set(null);
          this.client.set(null);
          this.vehicule.set(null);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // NAVIGATION
  // =========================================================

  goBack(): void {

    this.router.navigate([
      '/repair/tickets'
    ]);

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadDetails();

  }


  // =========================================================
  // DATE
  // =========================================================

  formatDate(
    date: string | null | undefined
  ): string {

    if (!date) {
      return '—';
    }

    const parsedDate =
      new Date(date);

    if (isNaN(parsedDate.getTime())) {
      return '—';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      }
    ).format(parsedDate);

  }


  formatTime(
    date: string | null | undefined
  ): string {

    if (!date) {
      return '—';
    }

    const parsedDate =
      new Date(date);

    if (isNaN(parsedDate.getTime())) {
      return '—';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ).format(parsedDate);

  }


  // =========================================================
  // PRICE
  // =========================================================

  formatPrice(
    price: number | null | undefined
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
  // STATUS
  // =========================================================

  getStatusClass(
    status: string | null | undefined
  ): string {

    return String(status ?? '')
      .toLowerCase()
      .replace(/[^a-z0-9]/g, '-');

  }


  getStatusIcon(
    status: string | null | undefined
  ): string {

    switch (
      String(status ?? '').toUpperCase()
      ) {

      case 'ACCEPTED':
        return 'check_circle';

      case 'REJECTED':
        return 'cancel';

      case 'PENDING':
        return 'schedule';

      case 'COMPLETED':
        return 'task_alt';

      default:
        return 'help_outline';

    }

  }

}
