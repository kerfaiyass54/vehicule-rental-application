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
  Router
} from '@angular/router';

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


@Component({
  selector: 'app-admin-clients',
  standalone: true,

  imports: [
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],

  templateUrl: './admin-clients.html',

  styleUrl: './admin-clients.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminClients
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly clientService =
    inject(ClientManagementService);

  private readonly router =
    inject(Router);

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


  ngAfterViewInit(): void {

    this.setupRevealAnimation();

  }


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
  // CREATE CLIENT
  // =========================================================

  addClient(): void {

    this.router.navigate([
      '/admin/clients/add-client'
    ]);

  }


  // =========================================================
  // VIEW CLIENT
  // =========================================================

  viewClient(
    client: ClientAdmin
  ): void {

    this.router.navigate([
      '/admin/clients',
      client.id
    ]);

  }


  // =========================================================
  // EDIT CLIENT
  // =========================================================

  editClient(
    client: ClientAdmin
  ): void {

    this.router.navigate([
      '/admin/clients',
      client.id,
      'edit'
    ]);

  }


  // =========================================================
  // DELETE CLIENT
  // =========================================================

  deleteClient(
    client: ClientAdmin
  ): void {

    const confirmed =
      window.confirm(
        `Are you sure you want to delete ${client.nameClient}?`
      );


    if (!confirmed) {

      return;

    }


    this.clientService

      .deleteClient(client.id)

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: () => {

          this.loadClients();

        },

        error: error => {

          console.error(
            'Unable to delete client:',
            error
          );

        }

      });

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
