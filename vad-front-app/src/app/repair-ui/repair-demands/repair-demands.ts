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

import {
  MatTableModule
} from '@angular/material/table';

import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatDialog,
  MatDialogModule
} from '@angular/material/dialog';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  RepairDemandService
} from '../../services/repair-services/repair-demand.service';



import {
  DemandDetails
} from '../models/demand-details.model';
import {DemandsListPage} from '../models/DemandsListPage';
import {DemandDetailsDialog} from './demand-details-dialog/demand-details-dialog';




@Component({
  selector: 'app-repair-demands',

  standalone: true,

  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatTooltipModule
  ],

  templateUrl: './repair-demands.html',
  styleUrl: './repair-demands.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RepairDemands
  implements OnInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly demandService =
    inject(RepairDemandService);

  private readonly dialog =
    inject(MatDialog);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // DESTROY
  // =========================================================

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly demands =
    signal<DemandsListPage[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly repairEmail =
    signal('');

  readonly page =
    signal(0);

  readonly size =
    signal(10);

  readonly totalElements =
    signal(0);


  // =========================================================
  // TABLE
  // =========================================================

  readonly displayedColumns = [
    'id',
    'type',
    'date',
    'status',
    'action'
  ];


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

  }


  // =========================================================
  // DESTROY
  // =========================================================

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
        'Repair email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.repairEmail.set(email);

    this.loadDemands();

  }


  // =========================================================
  // LOAD DEMANDS
  // =========================================================

  loadDemands(): void {

    const email =
      this.repairEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.demandService
      .getDemands(
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

          this.demands.set(
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
            'Unable to load repair demands:',
            error
          );

          this.demands.set([]);

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

    this.loadDemands();

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

    this.loadDemands();

  }


  // =========================================================
  // OPEN DETAILS
  // =========================================================

  openDetails(
    demand: DemandsListPage
  ): void {

    this.demandService
      .getDemandDetails(demand.id)

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: details => {

          this.openDetailsDialog(details);

        },

        error: error => {

          console.error(
            'Unable to load demand details:',
            error
          );

        }

      });

  }


  // =========================================================
  // DIALOG
  // =========================================================

  private openDetailsDialog(
    demand: DemandDetails
  ): void {

    this.dialog.open(
      DemandDetailsDialog,
      {
        width: '700px',
        maxWidth: '95vw',
        maxHeight: '90vh',

        data: demand,

        panelClass: 'demand-details-dialog',

        autoFocus: false
      }
    );

  }


  // =========================================================
  // TRACK
  // =========================================================

  trackDemand(
    _index: number,
    demand: DemandsListPage
  ): number {

    return demand.id;

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
  // STATUS
  // =========================================================

  getStatusClass(
    status: string
  ): string {

    switch (String(status).toUpperCase()) {

      case 'APPROVED':
        return 'status-approved';

      case 'REFUSED':
        return 'status-refused';

      case 'PENDING':
        return 'status-pending';

      default:
        return 'status-default';

    }

  }


  getStatusIcon(
    status: string
  ): string {

    switch (String(status).toUpperCase()) {

      case 'APPROVED':
        return 'check_circle';

      case 'REFUSED':
        return 'cancel';

      case 'PENDING':
        return 'schedule';

      default:
        return 'help_outline';

    }

  }


  // =========================================================
  // TYPE
  // =========================================================

  getTypeIcon(
    type: string
  ): string {

    const value =
      String(type).toUpperCase();

    if (value.includes('URGENT')) {
      return 'priority_high';
    }

    if (value.includes('MAINTENANCE')) {
      return 'build';
    }

    if (value.includes('MODIFICATION')) {
      return 'construction';
    }

    return 'car_repair';

  }

}
