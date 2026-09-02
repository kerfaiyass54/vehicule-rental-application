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

import { FormsModule } from '@angular/forms';

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
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  RepairManagementService
} from '../../services/admin-services/repair-management.service';

import {
  RepairAdmin
} from '../models/repair-admin.model';


@Component({
  selector: 'app-admin-repairs',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './admin-repairs.html',

  styleUrl: './admin-repairs.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminRepairs
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly repairService =
    inject(RepairManagementService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly repairs =
    signal<RepairAdmin[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly page =
    signal(0);

  readonly size =
    signal(5);

  readonly totalElements =
    signal(0);

  readonly searchTerm =
    signal('');


  // =========================================================
  // DIALOG
  // =========================================================

  activeDialog:
    'view' |
    'update' |
    'delete' |
    null = null;

  selectedRepair:
    RepairAdmin | null = null;

  dialogLoading = false;

  dialogSaving = false;

  dialogDeleting = false;

  dialogError = '';

  successMessage = '';


  // =========================================================
  // UPDATE FORM
  // =========================================================

  updateForm = {

    nameRepair: '',

    email: '',

    role: '',

    locationId: 0,

    locationName: ''

  };


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadRepairs();

  }


  ngAfterViewInit(): void {

    this.setupRevealAnimation();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD REPAIRS
  // =========================================================

  loadRepairs(): void {

    this.loading.set(true);

    this.error.set(false);

    this.cdr.markForCheck();


    this.repairService

      .getRepairs(
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

          this.repairs.set(
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
            'Unable to load repair centers:',
            err
          );

          this.repairs.set([]);

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

    this.loadRepairs();

  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    this.searchTerm.set(
      input.value
        .trim()
        .toLowerCase()
    );

  }


  clearSearch(): void {

    this.searchTerm.set('');

  }


  // =========================================================
  // FILTERED REPAIRS
  // =========================================================

  get filteredRepairs(): RepairAdmin[] {

    const search =
      this.searchTerm();


    if (!search) {

      return this.repairs();

    }


    return this.repairs().filter(
      repair => {

        const values = [

          repair.id,

          repair.nameRepair,

          repair.email,

          repair.locationName

        ];


        return values.some(
          value =>
            String(value ?? '')
              .toLowerCase()
              .includes(search)
        );

      }
    );

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

    this.loadRepairs();


    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================================
  // VIEW
  // =========================================================

  viewRepair(
    repair: RepairAdmin
  ): void {

    if (!repair?.id) {

      return;

    }

    this.selectedRepair =
      repair;

    this.activeDialog =
      'view';

    this.dialogLoading =
      true;

    this.dialogError =
      '';

    this.cdr.markForCheck();


    this.repairService

      .getRepair(
        repair.id
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.dialogLoading =
            false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: details => {

          this.selectedRepair =
            details;

          this.cdr.markForCheck();

        },

        error: err => {

          console.error(
            'Unable to load repair center details:',
            err
          );

          this.dialogError =
            'Unable to load the latest repair center information.';

          this.selectedRepair =
            repair;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // EDIT
  // =========================================================

  editRepair(
    repair: RepairAdmin
  ): void {

    if (!repair?.id) {

      return;

    }

    this.selectedRepair =
      repair;

    this.activeDialog =
      'update';

    this.dialogLoading =
      true;

    this.dialogSaving =
      false;

    this.dialogError =
      '';

    this.cdr.markForCheck();


    this.repairService

      .getRepair(
        repair.id
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.dialogLoading =
            false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: details => {

          this.selectedRepair =
            details;

          this.fillUpdateForm(
            details
          );

          this.cdr.markForCheck();

        },

        error: err => {

          console.error(
            'Unable to load repair center for update:',
            err
          );

          this.fillUpdateForm(
            repair
          );

          this.dialogError =
            'Unable to load the latest repair center information.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // FILL UPDATE FORM
  // =========================================================

  private fillUpdateForm(
    repair: RepairAdmin
  ): void {

    this.updateForm = {

      nameRepair:
        repair.nameRepair ?? '',

      email:
        repair.email ?? '',

      role:
        repair.role ?? '',

      locationId:
        Number(
          repair.locationId ?? 0
        ),

      locationName:
        repair.locationName ?? ''

    };

  }


  // =========================================================
  // SAVE UPDATE
  // =========================================================

  saveRepairUpdate(): void {

    if (
      !this.selectedRepair ||
      this.dialogSaving
    ) {

      return;

    }


    this.dialogError =
      '';


    const nameRepair =
      this.updateForm.nameRepair
        .trim();

    const email =
      this.updateForm.email
        .trim();

    const role =
      this.updateForm.role
        .trim();


    // =======================================================
    // VALIDATION
    // =======================================================

    if (!nameRepair) {

      this.dialogError =
        'Repair center name is required.';

      return;

    }


    if (!email) {

      this.dialogError =
        'Email address is required.';

      return;

    }


    if (!this.isValidEmail(email)) {

      this.dialogError =
        'Please enter a valid email address.';

      return;

    }


    if (!role) {

      this.dialogError =
        'Role is required.';

      return;

    }


    // =======================================================
    // REQUEST
    // =======================================================

    this.dialogSaving =
      true;

    this.cdr.markForCheck();


    const updatedRepair: RepairAdmin = {

      id:
      this.selectedRepair.id,

      nameRepair,

      email,

      role,

      locationId:
      this.updateForm.locationId,

      locationName:
      this.updateForm.locationName

    };


    this.repairService

      .updateRepair(

        this.selectedRepair.id,

        updatedRepair

      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.dialogSaving =
            false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.closeDialog();

          this.successMessage =
            'Repair center updated successfully.';

          this.loadRepairs();

          this.cdr.markForCheck();


          setTimeout(() => {

            this.successMessage =
              '';

            this.cdr.markForCheck();

          }, 3000);

        },

        error: err => {

          console.error(
            'Unable to update repair center:',
            err
          );

          this.dialogError =
            'Unable to update the repair center.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // DELETE
  // =========================================================

  deleteRepair(
    repair: RepairAdmin
  ): void {

    if (!repair?.id) {

      return;

    }


    this.selectedRepair =
      repair;

    this.activeDialog =
      'delete';

    this.dialogDeleting =
      false;

    this.dialogError =
      '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // CONFIRM DELETE
  // =========================================================

  confirmDelete(): void {

    if (
      !this.selectedRepair ||
      this.dialogDeleting
    ) {

      return;

    }


    this.dialogDeleting =
      true;

    this.dialogError =
      '';

    this.cdr.markForCheck();


    this.repairService

      .deleteRepair(
        this.selectedRepair.id
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.dialogDeleting =
            false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.closeDialog();

          this.successMessage =
            'Repair center deleted successfully.';

          /*
           * If the current page contained only the
           * deleted item, move to the previous page.
           */

          if (
            this.repairs().length === 1 &&
            this.page() > 0
          ) {

            this.page.update(
              value => value - 1
            );

          }

          this.loadRepairs();

          this.cdr.markForCheck();


          setTimeout(() => {

            this.successMessage =
              '';

            this.cdr.markForCheck();

          }, 3000);

        },

        error: err => {

          console.error(
            'Unable to delete repair center:',
            err
          );

          this.dialogError =
            'Unable to delete the repair center.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // CLOSE DIALOG
  // =========================================================

  closeDialog(): void {

    if (
      this.dialogSaving ||
      this.dialogDeleting
    ) {

      return;

    }


    this.activeDialog =
      null;

    this.selectedRepair =
      null;

    this.dialogLoading =
      false;

    this.dialogError =
      '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // DIALOG BACKDROP
  // =========================================================

  onDialogBackdropClick(
    event: MouseEvent
  ): void {

    if (
      event.target ===
      event.currentTarget
    ) {

      this.closeDialog();

    }

  }


  // =========================================================
  // EMAIL VALIDATION
  // =========================================================

  isValidEmail(
    email: string
  ): boolean {

    return /^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$/
      .test(email);

  }


  // =========================================================
  // INITIALS
  // =========================================================

  getInitials(
    name: string
  ): string {

    if (!name) {

      return 'RC';

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


  // =========================================================
  // TRACK
  // =========================================================

  trackRepair(
    _index: number,
    repair: RepairAdmin
  ): number {

    return repair.id ?? _index;

  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private setupRevealAnimation(): void {

    const elements =
      document.querySelectorAll(
        '.reveal'
      );


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

            }

          });

        },

        {
          threshold: 0.08
        }

      );


    elements.forEach(element => {

      observer.observe(element);

    });

  }

}
