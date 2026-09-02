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
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  RepairManagementService
} from '../../services/admin-services/repair-management.service';


@Component({
  selector: 'app-admin-repairs',

  standalone: true,

  imports: [
    CommonModule,
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
    signal<any[]>([]);

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
  // LIFECYCLE
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
      input.value.trim().toLowerCase()
    );

  }


  clearSearch(): void {

    this.searchTerm.set('');

  }


  get filteredRepairs(): any[] {

    const search =
      this.searchTerm();


    if (!search) {

      return this.repairs();

    }


    return this.repairs().filter(
      repair => {

        const values = [

          repair.idRepair,

          repair.name,

          repair.address,

          repair.city,

          repair.country,

          repair.phone,

          repair.email

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
  // ACTIONS
  // =========================================================

  viewRepair(
    repair: any
  ): void {

    console.log(
      'View repair center:',
      repair
    );

  }


  editRepair(
    repair: any
  ): void {

    console.log(
      'Edit repair center:',
      repair
    );

  }


  deleteRepair(
    repair: any
  ): void {

    if (!repair?.idRepair) {

      return;

    }


    const confirmed =
      window.confirm(
        `Are you sure you want to delete "${repair.name}"?`
      );


    if (!confirmed) {

      return;

    }


    this.repairService

      .deleteRepair(
        repair.idRepair
      )

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: () => {

          this.loadRepairs();

        },

        error: err => {

          console.error(
            'Unable to delete repair center:',
            err
          );

        }

      });

  }


  // =========================================================
  // HELPERS
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


  getRepairName(
    repair: any
  ): string {

    return repair?.name
      || repair?.repairName
      || 'Repair center';

  }


  getAddress(
    repair: any
  ): string {

    return repair?.address
      || repair?.position
      || repair?.location
      || '—';

  }


  getPhone(
    repair: any
  ): string {

    return repair?.phone
      || repair?.telephone
      || '—';

  }


  getEmail(
    repair: any
  ): string {

    return repair?.email
      || '—';

  }


  trackRepair(
    _index: number,
    repair: any
  ): number {

    return repair?.idRepair ?? _index;

  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private setupRevealAnimation(): void {

    const elements =
      document.querySelectorAll('.reveal');


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
