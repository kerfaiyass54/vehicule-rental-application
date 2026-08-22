import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  QueryList,
  ViewChildren,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';
import Keycloak from 'keycloak-js';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';

import {
  Subject,
  finalize,
  forkJoin,
  takeUntil
} from 'rxjs';

import { SupplierVehicule } from '../../services/supplier-services/supplier-vehicule';

import { Vehicule } from '../models/vehicule.model';
import { VehiculeList } from '../models/vehicule-list.model';
import { VehiculeStatus } from '../models/vehicule-status.enum';
import { Transmission } from '../models/transmission.enum';

Chart.register(...registerables);

@Component({
  selector: 'app-supplier-vehicules',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatChipsModule
  ],

  templateUrl: './supplier-vehicules.html',
  styleUrl: './supplier-vehicules.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierVehicules
  implements OnInit, AfterViewInit, OnDestroy {

  // ---------------------------------------------------------
  // DEPENDENCIES
  // ---------------------------------------------------------

  private readonly keycloak = inject(Keycloak);

  private readonly supplierVehiculeService =
    inject(SupplierVehicule);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // ---------------------------------------------------------
  // SCROLL REVEAL
  // ---------------------------------------------------------

  @ViewChildren('revealElement', {
    read: ElementRef
  })
  private revealElements!: QueryList<ElementRef>;

  private observer?: IntersectionObserver;


  // ---------------------------------------------------------
  // SUPPLIER
  // ---------------------------------------------------------

  readonly supplierEmail =
    signal('');

  readonly supplierName =
    signal('Supplier');


  // ---------------------------------------------------------
  // GENERAL STATE
  // ---------------------------------------------------------

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly vehicles =
    signal<VehiculeList[]>([]);


  // ---------------------------------------------------------
  // STATISTICS
  // ---------------------------------------------------------

  readonly totalVehicles =
    signal(0);

  readonly availableVehicles =
    signal(0);

  readonly takenVehicles =
    signal(0);

  readonly repairVehicles =
    signal(0);


  // ---------------------------------------------------------
  // PAGINATION
  // ---------------------------------------------------------

  readonly page =
    signal(0);

  readonly size =
    signal(6);

  readonly totalElements =
    signal(0);


  // ---------------------------------------------------------
  // CHARTS
  // ---------------------------------------------------------

  private charts: Chart[] = [];


  // ---------------------------------------------------------
  // LIFECYCLE
  // ---------------------------------------------------------

  ngOnInit(): void {
    this.loadSupplier();
  }

  ngAfterViewInit(): void {

    this.initializeScrollReveal();

    this.revealElements.changes
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.initializeScrollReveal();
      });
  }

  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

    this.observer?.disconnect();

    this.destroyCharts();
  }


  // ---------------------------------------------------------
  // SUPPLIER
  // ---------------------------------------------------------

  private loadSupplier(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    const username =
      token?.['preferred_username'] ?? '';

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

    this.supplierName.set(
      username || email.split('@')[0]
    );

    this.loadDashboard();
  }


  // ---------------------------------------------------------
  // DASHBOARD
  // ---------------------------------------------------------

  private loadDashboard(): void {

    const email =
      this.supplierEmail();

    this.loading.set(true);
    this.error.set(false);

    forkJoin({

      total:
        this.supplierVehiculeService
          .getTotalVehicles(email),

      available:
        this.supplierVehiculeService
          .countVehiclesByStatus(
            email,
            VehiculeStatus.AVAILABLE
          ),

      taken:
        this.supplierVehiculeService
          .countVehiclesByStatus(
            email,
            VehiculeStatus.TAKEN
          ),

      repair:
        this.supplierVehiculeService
          .countVehiclesByStatus(
            email,
            VehiculeStatus.REPARATION
          )

    })
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: stats => {

          this.totalVehicles.set(stats.total);
          this.availableVehicles.set(stats.available);
          this.takenVehicles.set(stats.taken);
          this.repairVehicles.set(stats.repair);

          this.loadVehicles();
        },

        error: error => {

          console.error(
            'Unable to load supplier vehicle statistics',
            error
          );

          this.error.set(true);
          this.loading.set(false);

          this.cdr.markForCheck();
        }
      });
  }


  // ---------------------------------------------------------
  // VEHICLES
  // ---------------------------------------------------------

  private loadVehicles(): void {

    const supplier =
      this.supplierEmail();

    this.supplierVehiculeService
      .getVehiclesPaged(
        supplier,
        this.page(),
        this.size()
      )
      .pipe(
        takeUntil(this.destroy$)
      )
      .subscribe({

        next: response => {

          this.vehicles.set(
            response.content ?? []
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.error.set(false);

          setTimeout(() => {
            this.createCharts();
            this.initializeScrollReveal();
          });

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load supplier vehicles',
            error
          );

          this.vehicles.set([]);
          this.totalElements.set(0);
          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // ---------------------------------------------------------
  // REFRESH
  // ---------------------------------------------------------

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadDashboard();
  }


  // ---------------------------------------------------------
  // PAGINATION
  // ---------------------------------------------------------

  onPageChange(
    event: PageEvent
  ): void {

    this.page.set(
      event.pageIndex
    );

    this.size.set(
      event.pageSize
    );

    this.loadVehicles();
  }


  // ---------------------------------------------------------
  // STATUS
  // ---------------------------------------------------------

  getStatusLabel(
    status: VehiculeStatus
  ): string {

    switch (status) {

      case VehiculeStatus.AVAILABLE:
        return 'Available';

      case VehiculeStatus.TAKEN:
        return 'Taken';

      case VehiculeStatus.REPARATION:
        return 'Repair';

      default:
        return status;
    }
  }


  getStatusIcon(
    status: VehiculeStatus
  ): string {

    switch (status) {

      case VehiculeStatus.AVAILABLE:
        return 'check_circle';

      case VehiculeStatus.TAKEN:
        return 'directions_car';

      case VehiculeStatus.REPARATION:
        return 'build';

      default:
        return 'info';
    }
  }


  getStatusClass(
    status: VehiculeStatus
  ): string {

    switch (status) {

      case VehiculeStatus.AVAILABLE:
        return 'status-available';

      case VehiculeStatus.TAKEN:
        return 'status-taken';

      case VehiculeStatus.REPARATION:
        return 'status-repair';

      default:
        return '';
    }
  }


  // ---------------------------------------------------------
  // TRANSMISSION
  // ---------------------------------------------------------

  getTransmissionLabel(
    transmission: Transmission
  ): string {

    return transmission === Transmission.AUTOMATIC
      ? 'Automatic'
      : 'Manual';
  }


  // ---------------------------------------------------------
  // CHARTS
  // ---------------------------------------------------------

  private createCharts(): void {

    this.destroyCharts();

    this.createStatusChart();

    this.createOverviewChart();
  }


  private createStatusChart(): void {

    const canvas =
      document.getElementById(
        'vehicleStatusChart'
      ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

    const configuration:
      ChartConfiguration<'doughnut'> = {

      type: 'doughnut',

      data: {

        labels: [
          'Available',
          'Taken',
          'Repair'
        ],

        datasets: [
          {
            data: [
              this.availableVehicles(),
              this.takenVehicles(),
              this.repairVehicles()
            ],

            borderWidth: 0,

            spacing: 5,

            hoverOffset: 12
          }
        ]
      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        cutout: '72%',

        animation: {
          duration: 900
        },

        plugins: {

          legend: {

            position: 'bottom',

            labels: {
              usePointStyle: true,
              padding: 18
            }
          },

          tooltip: {

            displayColors: false,

            callbacks: {

              label: context =>
                ` ${context.label}: ${context.parsed}`
            }
          }
        }
      }
    };

    this.charts.push(
      new Chart(
        canvas,
        configuration
      )
    );
  }


  private createOverviewChart(): void {

    const canvas =
      document.getElementById(
        'vehicleOverviewChart'
      ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

    const configuration:
      ChartConfiguration<'bar'> = {

      type: 'bar',

      data: {

        labels: [
          'Available',
          'Taken',
          'Repair'
        ],

        datasets: [

          {
            label: 'Vehicles',

            data: [
              this.availableVehicles(),
              this.takenVehicles(),
              this.repairVehicles()
            ],

            borderRadius: 10,

            borderSkipped: false,

            barThickness: 42
          }

        ]
      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        animation: {
          duration: 900
        },

        plugins: {

          legend: {
            display: false
          },

          tooltip: {
            displayColors: false
          }
        },

        scales: {

          x: {
            grid: {
              display: false
            }
          },

          y: {

            beginAtZero: true,

            ticks: {
              precision: 0
            },

            grid: {
              color: 'rgba(0,0,0,0.06)'
            }
          }
        }
      }
    };

    this.charts.push(
      new Chart(
        canvas,
        configuration
      )
    );
  }


  private destroyCharts(): void {

    this.charts.forEach(
      chart => chart.destroy()
    );

    this.charts = [];
  }


  // ---------------------------------------------------------
  // SCROLL REVEAL
  // ---------------------------------------------------------

  private initializeScrollReveal(): void {

    if (!this.revealElements) {
      return;
    }

    this.observer?.disconnect();

    this.observer =
      new IntersectionObserver(

        entries => {

          entries.forEach(entry => {

            const element =
              entry.target as HTMLElement;

            if (entry.isIntersecting) {

              element.classList.add(
                'is-visible'
              );

            } else {

              element.classList.remove(
                'is-visible'
              );
            }
          });
        },

        {
          threshold: 0.12
        }
      );

    this.revealElements.forEach(
      element => {

        this.observer?.observe(
          element.nativeElement
        );
      }
    );
  }


  // ---------------------------------------------------------
  // TRACKING
  // ---------------------------------------------------------

  trackVehicle(
    _index: number,
    vehicle: VehiculeList
  ): number {

    return vehicle.idVehicule;
  }
}
