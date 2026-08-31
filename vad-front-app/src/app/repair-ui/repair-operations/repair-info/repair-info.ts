import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { RepairOperationsService }
  from '../../../services/repair-services/repair-operations.service';

import { RepairInfoModel }
  from '../../models/repair-info.model';


@Component({
  selector: 'app-repair-info',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './repair-info.html',
  styleUrl: './repair-info.css',
})
export class RepairInfo implements OnInit {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly repairOperationsService =
    inject(RepairOperationsService);


  // =========================================================
  // DATA
  // =========================================================

  repair: RepairInfoModel | null = null;

  errorMessage = '';

  actionInProgress = false;


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    const idParam =
      this.route.snapshot.paramMap.get('id');

    const repairInfoId =
      Number(idParam);

    if (
      !idParam ||
      Number.isNaN(repairInfoId) ||
      repairInfoId <= 0
    ) {

      this.errorMessage =
        'Invalid repair operation ID.';

      return;
    }

    this.loadRepair(repairInfoId);
  }


  // =========================================================
  // LOAD REPAIR
  // =========================================================

  loadRepair(
    repairInfoId: number
  ): void {

    this.errorMessage = '';

    this.repairOperationsService
      .getRepairInfo(repairInfoId)
      .subscribe({

        next: (response) => {

          console.log(
            'Repair information:',
            response
          );

          this.repair = response;

        },

        error: (error) => {

          console.error(
            'Error loading repair information:',
            error
          );

          this.errorMessage =
            error?.error?.message ??
            'Unable to load repair information.';

        }

      });
  }


  // =========================================================
  // START REPAIR
  // =========================================================

  startRepair(): void {

    if (!this.repair) {
      return;
    }

    if (
      this.repair.repairStatus !==
      'PENDING_START'
    ) {
      return;
    }

    if (this.actionInProgress) {
      return;
    }

    this.actionInProgress = true;

    this.errorMessage = '';

    this.repairOperationsService
      .startRepair(
        this.repair.idRepairInfo
      )
      .subscribe({

        next: (updatedRepair) => {

          this.repair =
            updatedRepair;

          this.actionInProgress = false;

        },

        error: (error) => {

          console.error(
            'Error starting repair:',
            error
          );

          this.errorMessage =
            error?.error?.message ??
            'Unable to start the repair.';

          this.actionInProgress = false;

        }

      });
  }


  // =========================================================
  // CANCEL REPAIR
  // =========================================================

  cancelRepair(): void {

    if (!this.repair) {
      return;
    }

    if (
      this.repair.repairStatus !==
      'PENDING_FINISH'
    ) {
      return;
    }

    if (this.actionInProgress) {
      return;
    }

    this.actionInProgress = true;

    this.errorMessage = '';

    this.repairOperationsService
      .cancelRepair(
        this.repair.idRepairInfo
      )
      .subscribe({

        next: () => {

          if (this.repair) {

            this.repair = {
              ...this.repair,

              repairStatus:
                'CANCELLED' as any
            };

          }

          this.actionInProgress = false;

        },

        error: (error) => {

          console.error(
            'Error cancelling repair:',
            error
          );

          this.errorMessage =
            error?.error?.message ??
            'Unable to cancel the repair.';

          this.actionInProgress = false;

        }

      });
  }


  // =========================================================
  // STATUS
  // =========================================================

  isPendingStart(): boolean {

    return this.repair?.repairStatus ===
      'PENDING_START';
  }


  isPendingFinish(): boolean {

    return this.repair?.repairStatus ===
      'PENDING_FINISH';
  }


  isCancelled(): boolean {

    return this.repair?.repairStatus ===
      'CANCELLED';
  }


  isFinished(): boolean {

    return this.repair?.repairStatus ===
      'FINISHED';
  }


  // =========================================================
  // STATUS LABEL
  // =========================================================

  getStatusLabel(): string {

    if (!this.repair) {
      return '';
    }

    switch (this.repair.repairStatus) {

      case 'PENDING_START':
        return 'Pending start';

      case 'PENDING_FINISH':
        return 'Pending finish';

      case 'CANCELLED':
        return 'Cancelled';

      case 'FINISHED':
        return 'Finished';

      default:
        return this.repair.repairStatus;
    }
  }


  // =========================================================
  // STATUS CLASS
  // =========================================================

  getStatusClass(): string {

    if (!this.repair) {
      return '';
    }

    switch (this.repair.repairStatus) {

      case 'PENDING_START':
        return 'status-pending';

      case 'PENDING_FINISH':
        return 'status-progress';

      case 'CANCELLED':
        return 'status-cancelled';

      case 'FINISHED':
        return 'status-finished';

      default:
        return '';
    }
  }


  // =========================================================
  // FORMAT DATE
  // =========================================================

  formatDate(
    date: string | Date | null | undefined
  ): string {

    if (!date) {
      return '—';
    }

    return new Date(date).toLocaleString(
      'en-US',
      {
        dateStyle: 'medium',
        timeStyle: 'short'
      }
    );
  }


  // =========================================================
  // BACK
  // =========================================================

  goBack(): void {

    this.router.navigate([
      '/repair/operations'
    ]);
  }

}
