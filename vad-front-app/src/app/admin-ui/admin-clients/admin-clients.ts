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

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

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
  MatTableModule
} from '@angular/material/table';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  ClientAdmin
} from '../models/client-admin.model';

import {
  ClientManagementService
} from '../../services/admin-services/client-management.service';
import {NATIONALITIES} from '../constants/nationalities';




// =========================================================
// DIALOG TYPE
// =========================================================

type DialogType =
  | 'details'
  | 'update'
  | 'delete'
  | null;


// =========================================================
// COMPONENT
// =========================================================

@Component({
  selector: 'app-admin-clients',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],

  templateUrl: './admin-clients.html',

  styleUrl: './admin-clients.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class AdminClients
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly clientService =
    inject(ClientManagementService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly clients =
    signal<ClientAdmin[]>([]);

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
  // SUCCESS MESSAGE
  // =========================================================

  successMessage = '';


  // =========================================================
  // DIALOG
  // =========================================================

  activeDialog:
    DialogType = null;

  selectedClient:
    ClientAdmin | null = null;

  dialogLoading = false;

  dialogSaving = false;

  dialogDeleting = false;

  dialogError = '';


  // =========================================================
  // UPDATE FORM
  // =========================================================

  updateForm = {

    id: 0,

    nameClient: '',

    email: '',

    nationality: '',

    budget: 0,

    locationId: 0,

    locationName: ''

  };


  // =========================================================
  // NATIONALITIES
  // =========================================================

  readonly nationalities =
    NATIONALITIES;


  // =========================================================
  // TABLE
  // =========================================================

  readonly displayedColumns = [

    'client',

    'email',

    'nationality',

    'budget',

    'location',

    'actions'

  ];


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadClients();

  }


  // =========================================================
  // AFTER VIEW INIT
  // =========================================================

  ngAfterViewInit(): void {

    this.setupRevealAnimation();

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD CLIENTS
  // =========================================================

  loadClients(): void {

    this.loading.set(true);

    this.error.set(false);

    this.cdr.markForCheck();


    this.clientService

      .getClients(
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

          this.clients.set(
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
            'Unable to load clients:',
            error
          );

          this.clients.set([]);

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

    this.loadClients();

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
    );

  }


  // =========================================================
  // FILTERED CLIENTS
  // =========================================================

  get filteredClients(): ClientAdmin[] {

    const term =
      this.searchTerm()
        .trim()
        .toLowerCase();


    if (!term) {

      return this.clients();

    }


    return this.clients().filter(
      client =>

        client.nameClient
          ?.toLowerCase()
          .includes(term)

        ||

        client.email
          ?.toLowerCase()
          .includes(term)

        ||

        client.nationality
          ?.toLowerCase()
          .includes(term)

        ||

        client.locationName
          ?.toLowerCase()
          .includes(term)

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

    this.loadClients();

    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================================
  // DETAILS DIALOG
  // =========================================================

  openDetails(
    client: ClientAdmin
  ): void {

    this.selectedClient =
      client;

    this.activeDialog =
      'details';

    this.dialogLoading =
      true;

    this.dialogError = '';

    this.cdr.markForCheck();


    this.clientService

      .getClient(
        client.id
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

          this.selectedClient =
            details;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load client details:',
            error
          );

          /*
           * Use the already loaded table data
           * as fallback.
           */

          this.selectedClient =
            client;

          this.dialogError =
            'Unable to load the latest client information.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // UPDATE DIALOG
  // =========================================================

  openUpdate(
    client: ClientAdmin
  ): void {

    this.selectedClient =
      client;

    this.activeDialog =
      'update';

    this.dialogLoading =
      true;

    this.dialogSaving =
      false;

    this.dialogError = '';

    this.cdr.markForCheck();


    this.clientService

      .getClient(
        client.id
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

          this.selectedClient =
            details;

          this.fillUpdateForm(
            details
          );

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load client for update:',
            error
          );

          /*
           * Use currently loaded row as fallback.
           */

          this.fillUpdateForm(
            client
          );

          this.dialogError =
            'Unable to retrieve the latest client information.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // FILL UPDATE FORM
  // =========================================================

  private fillUpdateForm(
    client: ClientAdmin
  ): void {

    this.updateForm = {

      id:
      client.id,

      nameClient:
        client.nameClient ?? '',

      email:
        client.email ?? '',

      nationality:
        client.nationality ?? '',

      budget:
        Number(
          client.budget ?? 0
        ),

      locationId:
        Number(
          client.locationId ?? 0
        ),

      locationName:
        client.locationName ?? ''

    };

  }


  // =========================================================
  // UPDATE CLIENT
  // =========================================================

  updateClient(): void {

    if (
      !this.selectedClient ||
      this.dialogSaving
    ) {

      return;

    }


    this.dialogError = '';


    const nameClient =
      this.updateForm.nameClient.trim();

    const email =
      this.updateForm.email.trim();

    const nationality =
      this.updateForm.nationality.trim();

    const budget =
      Number(
        this.updateForm.budget
      );


    // =======================================================
    // VALIDATION
    // =======================================================

    if (!nameClient) {

      this.dialogError =
        'Client name is required.';

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


    if (!nationality) {

      this.dialogError =
        'Nationality is required.';

      return;

    }


    if (
      !Number.isFinite(budget) ||
      budget < 0
    ) {

      this.dialogError =
        'Budget must be zero or greater.';

      return;

    }


    // =======================================================
    // REQUEST
    // =======================================================

    this.dialogSaving =
      true;

    this.cdr.markForCheck();


    const clientToUpdate: ClientAdmin = {

      id:
      this.selectedClient.id,

      nameClient,

      email,

      nationality,

      budget,

      locationId:
      this.updateForm.locationId,

      locationName:
      this.updateForm.locationName

    };


    // =======================================================
    // API
    // =======================================================

    this.clientService

      .updateClient(

        this.selectedClient.id,

        clientToUpdate

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

        next: updatedClient => {

          this.closeDialog();

          this.successMessage =
            'Client updated successfully.';

          this.loadClients();

          this.cdr.markForCheck();


          setTimeout(() => {

            this.successMessage =
              '';

            this.cdr.markForCheck();

          }, 3000);

        },

        error: error => {

          console.error(
            'Unable to update client:',
            error
          );

          this.dialogError =
            'Unable to update the client.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // DELETE DIALOG
  // =========================================================

  openDelete(
    client: ClientAdmin
  ): void {

    this.selectedClient =
      client;

    this.activeDialog =
      'delete';

    this.dialogDeleting =
      false;

    this.dialogError = '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // CONFIRM DELETE
  // =========================================================

  confirmDelete(): void {

    if (
      !this.selectedClient ||
      this.dialogDeleting
    ) {

      return;

    }


    this.dialogDeleting =
      true;

    this.dialogError = '';

    this.cdr.markForCheck();


    this.clientService

      .deleteClient(
        this.selectedClient.id
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
            'Client deleted successfully.';


          /*
           * Move to the previous page when
           * the current page becomes empty.
           */

          if (
            this.clients().length === 1 &&
            this.page() > 0
          ) {

            this.page.update(
              value => value - 1
            );

          }


          this.loadClients();

          this.cdr.markForCheck();


          setTimeout(() => {

            this.successMessage =
              '';

            this.cdr.markForCheck();

          }, 3000);

        },

        error: error => {

          console.error(
            'Unable to delete client:',
            error
          );

          this.dialogError =
            'Unable to delete the client.';

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

    this.selectedClient =
      null;

    this.dialogError = '';

    this.dialogLoading =
      false;

    this.cdr.markForCheck();

  }


  // =========================================================
  // BACKDROP CLICK
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

    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      .test(email);

  }


  // =========================================================
  // INITIALS
  // =========================================================

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


  // =========================================================
  // BUDGET
  // =========================================================

  formatBudget(
    budget: number
  ): string {

    return new Intl.NumberFormat(
      'en-US',
      {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2
      }
    ).format(
      budget ?? 0
    );

  }


  // =========================================================
  // TRACKING
  // =========================================================

  trackClient(
    _index: number,
    client: ClientAdmin
  ): number {

    return client.id;

  }


  // =========================================================
  // REVEAL ANIMATION
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

          entries.forEach(
            entry => {

              if (
                entry.isIntersecting
              ) {

                entry.target.classList.add(
                  'visible'
                );

                observer.unobserve(
                  entry.target
                );

              }

            }
          );

        },

        {
          threshold: 0.08
        }

      );


    elements.forEach(
      element => {

        observer.observe(
          element
        );

      }
    );

  }

}
