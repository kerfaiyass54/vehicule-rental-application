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
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { SupplierDemand } from '../../services/supplier-services/supplier-demand';
import { DemandResponse } from '../models/demand-response.model';
import { ConfirmStatus } from '../models/confirm-status.enum';

@Component({
  selector: 'app-supplier-demands',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './supplier-demands.html',
  styleUrl: './supplier-demands.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierDemands implements OnInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly supplierDemandService =
    inject(SupplierDemand);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly demands =
    signal<DemandResponse[]>([]);

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

  readonly processingId =
    signal<number | null>(null);

  readonly processingAction =
    signal<'approve' | 'refuse' | null>(null);


  // =========================================================
  // STATUS
  // =========================================================

  readonly ConfirmStatus = ConfirmStatus;


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
  // KEYCLOAK
  // =========================================================

  private loadSupplierEmail(): void {

    const token = this.keycloak.tokenParsed;

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

    this.loadDemands();
  }


  // =========================================================
  // LOAD DEMANDS
  // =========================================================

  loadDemands(): void {

    const email = this.supplierEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();

    this.supplierDemandService
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
            'Unable to load supplier demands',
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

  onPageChange(event: PageEvent): void {

    this.page.set(event.pageIndex);

    this.size.set(event.pageSize);

    this.loadDemands();
  }


  // =========================================================
  // APPROVE
  // =========================================================

  approveDemand(
    demand: DemandResponse
  ): void {

    if (demand.status !== ConfirmStatus.PENDING) {
      return;
    }

    const confirmed = window.confirm(
      `Approve this demand?\n\n` +
      `Vehicle: ${demand.vehiculeName}\n` +
      `Repair: ${demand.repairName}`
    );

    if (!confirmed) {
      return;
    }

    this.processingId.set(
      demand.idDemand
    );

    this.processingAction.set(
      'approve'
    );

    this.cdr.markForCheck();

    this.supplierDemandService
      .approveDemand(demand.idDemand)
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.processingId.set(null);

          this.processingAction.set(null);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: updatedDemand => {

          this.updateDemand(updatedDemand);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to approve demand',
            error
          );

          window.alert(
            'Unable to approve this demand. Please try again.'
          );

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // REFUSE
  // =========================================================

  refuseDemand(
    demand: DemandResponse
  ): void {

    if (demand.status !== ConfirmStatus.PENDING) {
      return;
    }

    const confirmed = window.confirm(
      `Refuse this demand?\n\n` +
      `Vehicle: ${demand.vehiculeName}\n` +
      `Repair: ${demand.repairName}`
    );

    if (!confirmed) {
      return;
    }

    this.processingId.set(
      demand.idDemand
    );

    this.processingAction.set(
      'refuse'
    );

    this.cdr.markForCheck();

    this.supplierDemandService
      .refuseDemand(demand.idDemand)
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.processingId.set(null);

          this.processingAction.set(null);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: updatedDemand => {

          this.updateDemand(updatedDemand);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to refuse demand',
            error
          );

          window.alert(
            'Unable to refuse this demand. Please try again.'
          );

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // UPDATE LOCAL DEMAND
  // =========================================================

  private updateDemand(
    updatedDemand: DemandResponse
  ): void {

    this.demands.update(
      current =>
        current.map(demand =>
          demand.idDemand === updatedDemand.idDemand
            ? updatedDemand
            : demand
        )
    );
  }


  // =========================================================
  // HELPERS
  // =========================================================

  isProcessing(
    demand: DemandResponse
  ): boolean {

    return this.processingId() === demand.idDemand;
  }


  isApproving(
    demand: DemandResponse
  ): boolean {

    return (
      this.processingId() === demand.idDemand &&
      this.processingAction() === 'approve'
    );
  }


  isRefusing(
    demand: DemandResponse
  ): boolean {

    return (
      this.processingId() === demand.idDemand &&
      this.processingAction() === 'refuse'
    );
  }


  isPending(
    demand: DemandResponse
  ): boolean {

    return demand.status === ConfirmStatus.PENDING;
  }


  getStatusClass(
    status: ConfirmStatus
  ): string {

    switch (status) {

      case ConfirmStatus.APPROVED:
        return 'status-approved';

      case ConfirmStatus.REFUSED:
        return 'status-refused';

      case ConfirmStatus.PENDING:
        return 'status-pending';

      default:
        return '';
    }
  }


  getStatusIcon(
    status: ConfirmStatus
  ): string {

    switch (status) {

      case ConfirmStatus.APPROVED:
        return 'check_circle';

      case ConfirmStatus.REFUSED:
        return 'cancel';

      case ConfirmStatus.PENDING:
        return 'schedule';

      default:
        return 'help';
    }
  }


  getStatusLabel(
    status: ConfirmStatus
  ): string {

    switch (status) {

      case ConfirmStatus.APPROVED:
        return 'Approved';

      case ConfirmStatus.REFUSED:
        return 'Refused';

      case ConfirmStatus.PENDING:
        return 'Pending';

      default:
        return status;
    }
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
        part => part.charAt(0).toUpperCase()
      )
      .join('');
  }


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


  trackDemand(
    _index: number,
    demand: DemandResponse
  ): number {

    return demand.idDemand;
  }
}
