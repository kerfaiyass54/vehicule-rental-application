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

import {
  Router
} from '@angular/router';

import Keycloak from 'keycloak-js';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

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
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  ClientTicketService
} from '../../services/client-services/client-ticket.service';

import {
  TicketInfo
} from '../models/ticket-info.model';

import {
  RepairDemandStatus
} from '../enums/repair-demand-status';

import {
  TicketType
} from '../enums/ticket-type';


@Component({
  selector: 'app-client-tickets',

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

  templateUrl: './client-tickets.html',

  styleUrl: './client-tickets.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientTickets
  implements OnInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly ticketService =
    inject(ClientTicketService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly tickets =
    signal<TicketInfo[]>([]);

  readonly filteredTickets =
    signal<TicketInfo[]>([]);

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
    signal(5);

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

    this.loadTickets();

  }


  // =========================================================
  // LOAD TICKETS
  // =========================================================

  loadTickets(): void {

    const email =
      this.clientEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();


    this.ticketService
      .getTickets(
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

          this.tickets.set(content);

          this.filteredTickets.set(
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
            'Unable to load client tickets',
            error
          );

          this.tickets.set([]);
          this.filteredTickets.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // OPEN TICKET
  // =========================================================

  openTicket(): void {

    this.router.navigate([
      '/client/tickets/add'
    ]);

  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    const value =
      input.value.trim().toLowerCase();

    this.searchTerm.set(value);

    this.filteredTickets.set(
      this.applySearch(
        this.tickets(),
        value
      )
    );

  }


  private applySearch(
    tickets: TicketInfo[],
    search: string
  ): TicketInfo[] {

    if (!search) {
      return tickets;
    }

    return tickets.filter(ticket => {

      return (

        String(ticket.idTicket)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.type)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.description)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.status)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.repairName)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.vehiculeName)
          .toLowerCase()
          .includes(search)

      );

    });

  }


  clearSearch(): void {

    this.searchTerm.set('');

    this.filteredTickets.set(
      this.tickets()
    );

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadTickets();

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

    this.loadTickets();

  }


  // =========================================================
  // TRACK
  // =========================================================

  trackTicket(
    _index: number,
    ticket: TicketInfo
  ): number {

    return ticket.idTicket;

  }


  // =========================================================
  // STATUS
  // =========================================================

  getStatusClass(
    status: RepairDemandStatus
  ): string {

    switch (status) {

      case RepairDemandStatus.ACCEPTED:
        return 'status-accepted';

      case RepairDemandStatus.REJECTED:
        return 'status-rejected';

      case RepairDemandStatus.PENDING:
        return 'status-pending';

      default:
        return 'status-default';

    }

  }


  getStatusIcon(
    status: RepairDemandStatus
  ): string {

    switch (status) {

      case RepairDemandStatus.ACCEPTED:
        return 'check_circle';

      case RepairDemandStatus.REJECTED:
        return 'cancel';

      case RepairDemandStatus.PENDING:
        return 'schedule';

      default:
        return 'help_outline';

    }

  }


  // =========================================================
  // TYPE
  // =========================================================

  getTypeClass(
    type: TicketType
  ): string {

    return String(type)
      .toLowerCase()
      .replace(/[^a-z0-9]/g, '-');

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

}
