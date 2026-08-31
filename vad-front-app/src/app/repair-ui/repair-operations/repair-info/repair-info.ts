import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import {RepairOperationsService} from '../../../services/repair-services/repair-operations.service';
import {RepairInfoModel} from '../../models/repair-info.model';



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

  loading = false;

  errorMessage = '';


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    const id =
      Number(
        this.route.snapshot.paramMap.get('id')
      );

    if (!id) {

      this.errorMessage =
        'Invalid repair operation ID.';

      return;

    }

    this.loadRepair(id);

  }


  // =========================================================
  // LOAD
  // =========================================================

  loadRepair(
    repairInfoId: number
  ): void {

    this.loading = true;

    this.errorMessage = '';

    this.repairOperationsService
      .getRepairInfo(repairInfoId)
      .subscribe({

        next: (repair) => {

          this.repair = repair;

          this.loading = false;

        },

        error: (error) => {

          console.error(
            'Error loading repair:',
            error
          );

          this.errorMessage =
            'Unable to load repair information.';

          this.loading = false;

        }

      });

  }


  // =========================================================
  // START
  // =========================================================

  startRepair(): void {

    if (
      !this.repair ||
      this.repair.repairStatus !== 'PENDING_START'
    ) {
      return;
    }

    this.repairOperationsService
      .startRepair(
        this.repair.idRepairInfo
      )
      .subscribe({

        next: (updatedRepair) => {

          this.repair =
            updatedRepair;

        },

        error: (error) => {

          console.error(
            'Error starting repair:',
            error
          );

          this.errorMessage =
            'Unable to start the repair.';

        }

      });

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancelRepair(): void {

    if (
      !this.repair ||
      this.repair.repairStatus !== 'PENDING_FINISH'
    ) {
      return;
    }

    this.repairOperationsService
      .cancelRepair(
        this.repair.idRepairInfo
      )
      .subscribe({

        next: () => {

          if (this.repair) {

            this.repair.repairStatus =
              'CANCELLED' as any;

          }

        },

        error: (error) => {

          console.error(
            'Error cancelling repair:',
            error
          );

          this.errorMessage =
            'Unable to cancel the repair.';

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
  // BACK
  // =========================================================

  goBack(): void {

    this.router.navigate([
      '/repair/operations'
    ]);

  }

}
