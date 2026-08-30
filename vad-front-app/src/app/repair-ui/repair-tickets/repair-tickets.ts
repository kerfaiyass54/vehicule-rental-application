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

import { Router } from '@angular/router';

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

import { RepairTicketService } from '../../services/repair-services/repair-ticket.service';

import { RepairTicket } from '../models/repair-ticket.model';

import { RepairDemandStatus } from '../../client-ui/enums/repair-demand-status';

import { TicketType } from '../../client-ui/enums/ticket-type';


@Component({
  selector: 'app-repair-tickets',
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

  templateUrl: './repair-tickets.html',
  styleUrl: './repair-tickets.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RepairTickets implements OnInit, OnDestroy {

  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly ticketService =
    inject(RepairTicketService);

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
    signal<RepairTicket[]>([]);

  readonly filteredTickets =
    signal<RepairTicket[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly repairEmail =
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

    this.loadRepairEmail();

  }


  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    if (!email) {

      console.error(
        'Repair center email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.repairEmail.set(email);

    this.loadTickets();

  }


  // =========================================================
  // LOAD TICKETS
  // =========================================================

  loadTickets(): void {

    const email =
      this.repairEmail();

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
            'Unable to load repair tickets',
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
  // SEARCH
  // =========================================================

  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    const value =
      input.value
        .trim()
        .toLowerCase();

    this.searchTerm.set(value);

    this.filteredTickets.set(
      this.applySearch(
        this.tickets(),
        value
      )
    );

  }


  private applySearch(
    tickets: RepairTicket[],
    search: string
  ): RepairTicket[] {

    if (!search) {
      return tickets;
    }

    return tickets.filter(ticket => {

      return (

        String(ticket.id)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.type)
          .toLowerCase()
          .includes(search)

        ||

        String(ticket.status)
          .toLowerCase()
          .includes(search)

        ||

        this.formatDate(ticket.date)
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
  // OPEN TICKET DETAILS
  // =========================================================

  openTicket(
    ticket: RepairTicket
  ): void {

    this.router.navigate([
      '/repair/ticket',
      ticket.id
    ]);

  }


  // =========================================================
  // TRACK
  // =========================================================

  trackTicket(
    _index: number,
    ticket: RepairTicket
  ): number {

    return ticket.id;

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

      case RepairDemandStatus.COMPLETED:
        return 'status-completed';

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

      case RepairDemandStatus.COMPLETED:
        return 'task_alt';

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
      .replace(
        /[^a-z0-9]/g,
        '-'
      );

  }


  getTypeIcon(
    type: TicketType
  ): string {

    switch (type) {

      case TicketType.REPARATION:
        return 'build';

      case TicketType.MODIFICATION:
        return 'construction';

      default:
        return 'category';

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

}
