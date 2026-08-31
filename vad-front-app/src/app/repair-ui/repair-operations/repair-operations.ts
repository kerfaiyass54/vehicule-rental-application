import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import Keycloak from 'keycloak-js';

import { RepairOperationsService } from '../../services/repair-services/repair-operations.service';
import { RepairInfoModel } from '../models/repair-info.model';


@Component({
  selector: 'app-repair-operations',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './repair-operations.html',
  styleUrl: './repair-operations.css',
})
export class RepairOperations implements OnInit {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly repairOperationsService =
    inject(RepairOperationsService);

  private readonly router =
    inject(Router);

  private readonly keycloak =
    inject(Keycloak);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // DATA
  // =========================================================

  repairs: RepairInfoModel[] = [];

  loading = false;

  errorMessage = '';

  currentPage = 0;

  pageSize = 10;

  totalPages = 0;

  totalElements = 0;


  // =========================================================
  // REPAIR EMAIL
  // =========================================================

  repairEmail = '';


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

  }


  // =========================================================
  // GET REPAIR EMAIL FROM KEYCLOAK
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;


    // -------------------------------------------------------
    // EMAIL NOT FOUND
    // -------------------------------------------------------

    if (!email) {

      console.error(
        'Repair email could not be retrieved from Keycloak.'
      );

      this.errorMessage =
        'Unable to retrieve your repair center account information.';

      this.loading = false;

      this.cdr.markForCheck();

      return;

    }


    // -------------------------------------------------------
    // EMAIL FOUND
    // -------------------------------------------------------

    this.repairEmail = email;

    console.log(
      'Repair email retrieved from Keycloak:',
      this.repairEmail
    );


    // -------------------------------------------------------
    // LOAD REPAIRS
    // -------------------------------------------------------

    this.loadRepairs();

  }


  // =========================================================
  // LOAD REPAIRS
  // =========================================================

  loadRepairs(): void {

    this.loading = true;

    this.errorMessage = '';


    this.repairOperationsService
      .getRepairInfos(
        this.repairEmail,
        this.currentPage,
        this.pageSize
      )
      .subscribe({

        // ---------------------------------------------------
        // SUCCESS
        // ---------------------------------------------------

        next: (response) => {

          this.repairs =
            response.content;

          this.totalPages =
            response.totalPages;

          this.totalElements =
            response.totalElements;

          this.loading = false;

          this.cdr.markForCheck();

        },


        // ---------------------------------------------------
        // ERROR
        // ---------------------------------------------------

        error: (error) => {

          console.error(
            'Error loading repair operations:',
            error
          );

          this.errorMessage =
            'Unable to load repair operations. Please try again.';

          this.loading = false;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // OPEN DETAILS
  // =========================================================

  openRepair(
    repairInfoId: number
  ): void {

    this.router.navigate([
      '/repair/operations',
      repairInfoId
    ]);

  }


  // =========================================================
  // START REPAIR
  // =========================================================

  startRepair(
    event: Event,
    repairInfoId: number
  ): void {

    event.stopPropagation();

    this.repairOperationsService
      .startRepair(repairInfoId)
      .subscribe({

        next: (updatedRepair) => {

          const index =
            this.repairs.findIndex(
              repair =>
                repair.idRepairInfo === repairInfoId
            );


          if (index !== -1) {

            this.repairs[index] =
              updatedRepair;

          }

          this.cdr.markForCheck();

        },


        error: (error) => {

          console.error(
            'Error starting repair:',
            error
          );

          this.errorMessage =
            'Unable to start this repair.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // CANCEL REPAIR
  // =========================================================

  cancelRepair(
    event: Event,
    repairInfoId: number
  ): void {

    event.stopPropagation();

    this.repairOperationsService
      .cancelRepair(repairInfoId)
      .subscribe({

        next: () => {

          const repair =
            this.repairs.find(
              item =>
                item.idRepairInfo === repairInfoId
            );


          if (repair) {

            repair.repairStatus =
              'CANCELLED' as any;

          }

          this.cdr.markForCheck();

        },


        error: (error) => {

          console.error(
            'Error cancelling repair:',
            error
          );

          this.errorMessage =
            'Unable to cancel this repair.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // STATUS CHECKS
  // =========================================================

  isPendingStart(
    status: string
  ): boolean {

    return status === 'PENDING_START';

  }


  isPendingFinish(
    status: string
  ): boolean {

    return status === 'PENDING_FINISH';

  }


  isCancelled(
    status: string
  ): boolean {

    return status === 'CANCELLED';

  }


  isFinished(
    status: string
  ): boolean {

    return status === 'FINISHED';

  }


  // =========================================================
  // STATUS LABEL
  // =========================================================

  getStatusLabel(
    status: string
  ): string {

    switch (status) {

      case 'PENDING_START':
        return 'Pending start';

      case 'PENDING_FINISH':
        return 'Pending finish';

      case 'CANCELLED':
        return 'Cancelled';

      case 'FINISHED':
        return 'Finished';

      default:
        return status;

    }

  }


  // =========================================================
  // PAGINATION
  // =========================================================

  previousPage(): void {

    if (this.currentPage <= 0) {
      return;
    }

    this.currentPage--;

    this.loadRepairs();

  }


  nextPage(): void {

    if (
      this.currentPage >=
      this.totalPages - 1
    ) {
      return;
    }

    this.currentPage++;

    this.loadRepairs();

  }

}
