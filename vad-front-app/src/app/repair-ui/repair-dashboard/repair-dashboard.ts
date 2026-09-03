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
  MatIconModule
} from '@angular/material/icon';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import {
  RepairDetails as RepairDetailsService
} from '../../services/repair-services/repair-details';

import {
  RepairDashboard as RepairDashboardModel
} from '../models/repair-dashboard.model';


Chart.register(
  ...registerables
);


@Component({
  selector: 'app-repair-dashboard',

  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],

  templateUrl:
    './repair-dashboard.html',

  styleUrl:
    './repair-dashboard.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class RepairDashboard
  implements OnInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly repairService =
    inject(RepairDetailsService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // DESTROY
  // =========================================================

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // CHARTS
  // =========================================================

  private ticketChart?: Chart;

  private demandChart?: Chart;


  // =========================================================
  // STATE
  // =========================================================

  readonly dashboard =
    signal<RepairDashboardModel | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly repairEmail =
    signal('');


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

  }


  ngOnDestroy(): void {

    this.destroyCharts();

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // GET EMAIL FROM KEYCLOAK
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;


    if (
      !email ||
      !email.trim()
    ) {

      this.loading.set(false);

      this.error.set(true);

      this.cdr.markForCheck();

      return;

    }


    this.repairEmail.set(
      email.trim()
    );

    this.loadDashboard();

  }


  // =========================================================
  // LOAD DASHBOARD
  // =========================================================

  loadDashboard(): void {

    const email =
      this.repairEmail();


    if (!email) {

      return;

    }


    this.loading.set(true);

    this.error.set(false);


    this.repairService

      .getDashboard(
        email
      )

      .pipe(

        takeUntil(
          this.destroy$
        ),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: dashboard => {

          this.dashboard.set(
            dashboard
          );

          this.error.set(
            false
          );

          this.cdr.markForCheck();


          /*
           * Angular must render the canvas
           * before Chart.js initializes it.
           */

          setTimeout(() => {

            this.createCharts();

          });

        },


        error: error => {

          console.error(
            'Unable to load repair center dashboard:',
            error
          );

          this.dashboard.set(
            null
          );

          this.error.set(
            true
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (
      this.loading()
    ) {

      return;

    }


    this.destroyCharts();

    this.loadDashboard();

  }


  // =========================================================
  // CHARTS
  // =========================================================

  private createCharts(): void {

    const data =
      this.dashboard();


    if (!data) {

      return;

    }


    this.destroyCharts();

    this.createTicketChart(
      data
    );

    this.createDemandChart(
      data
    );

  }


  // =========================================================
  // TICKET CHART
  // =========================================================

  private createTicketChart(
    data: RepairDashboardModel
  ): void {

    const canvas =
      document.getElementById(
        'ticketChart'
      ) as HTMLCanvasElement | null;


    if (!canvas) {

      return;

    }


    const configuration:
      ChartConfiguration<'doughnut'> = {

      type:
        'doughnut',

      data: {

        labels: [
          'Pending',
          'Accepted',
          'Completed'
        ],

        datasets: [

          {

            data: [

              data.pendingTickets,

              data.acceptedTickets,

              data.completedTickets

            ],

            borderWidth:
              0,

            hoverOffset:
              8,

            backgroundColor: [

              '#f59e0b',

              '#6366f1',

              '#22c55e'

            ]

          }

        ]

      },

      options: {

        responsive:
          true,

        maintainAspectRatio:
          false,

        cutout:
          '68%',

        animation: {

          duration:
            950,

          easing:
            'easeOutQuart'

        },

        plugins: {

          legend: {

            position:
              'bottom',

            labels: {

              usePointStyle:
                true,

              pointStyle:
                'circle',

              padding:
                18,

              font: {

                size:
                  10

              }

            }

          }

        }

      }

    };


    this.ticketChart =
      new Chart(
        canvas,
        configuration
      );

  }


  // =========================================================
  // DEMAND CHART
  // =========================================================

  private createDemandChart(
    data: RepairDashboardModel
  ): void {

    const canvas =
      document.getElementById(
        'demandChart'
      ) as HTMLCanvasElement | null;


    if (!canvas) {

      return;

    }


    const configuration:
      ChartConfiguration<'bar'> = {

      type:
        'bar',

      data: {

        labels: [

          'Pending',

          'Accepted',

          'Rejected'

        ],

        datasets: [

          {

            label:
              'Demands',

            data: [

              data.pendingDemands,

              data.acceptedDemands,

              data.rejectedDemands

            ],

            backgroundColor: [

              '#f59e0b',

              '#6366f1',

              '#ef4444'

            ],

            borderRadius:
              10,

            borderSkipped:
              false,

            maxBarThickness:
              58

          }

        ]

      },

      options: {

        responsive:
          true,

        maintainAspectRatio:
          false,

        animation: {

          duration:
            950,

          easing:
            'easeOutQuart'

        },

        scales: {

          y: {

            beginAtZero:
              true,

            ticks: {

              precision:
                0,

              color:
                '#94a3b8',

              font: {

                size:
                  9

              }

            },

            grid: {

              color:
                '#eef2f7'

            }

          },

          x: {

            ticks: {

              color:
                '#64748b',

              font: {

                size:
                  9

              }

            },

            grid: {

              display:
                false

            }

          }

        },

        plugins: {

          legend: {

            display:
              false

          }

        }

      }

    };


    this.demandChart =
      new Chart(
        canvas,
        configuration
      );

  }


  // =========================================================
  // DESTROY CHARTS
  // =========================================================

  private destroyCharts(): void {

    this.ticketChart?.destroy();

    this.demandChart?.destroy();

    this.ticketChart =
      undefined;

    this.demandChart =
      undefined;

  }


  // =========================================================
  // TICKET STATISTICS
  // =========================================================

  get totalTickets(): number {

    return this.dashboard()
      ?.totalTickets ?? 0;

  }


  get pendingTickets(): number {

    return this.dashboard()
      ?.pendingTickets ?? 0;

  }


  get acceptedTickets(): number {

    return this.dashboard()
      ?.acceptedTickets ?? 0;

  }


  get completedTickets(): number {

    return this.dashboard()
      ?.completedTickets ?? 0;

  }


  // =========================================================
  // REPAIR STATISTICS
  // =========================================================

  get activeRepairs(): number {

    return this.dashboard()
      ?.activeRepairs ?? 0;

  }


  get completedRepairs(): number {

    return this.dashboard()
      ?.completedRepairs ?? 0;

  }


  get cancelledRepairs(): number {

    return this.dashboard()
      ?.cancelledRepairs ?? 0;

  }


  // =========================================================
  // TOTAL REPAIRS
  // =========================================================

  /*
   * RepairDashboard does not expose a totalRepairs field.
   *
   * The total is derived from the three available
   * repair states.
   */

  get totalRepairs(): number {

    return (
      this.activeRepairs +
      this.completedRepairs +
      this.cancelledRepairs
    );

  }


  // =========================================================
  // DEMAND STATISTICS
  // =========================================================

  get totalDemands(): number {

    return this.dashboard()
      ?.totalDemands ?? 0;

  }


  get pendingDemands(): number {

    return this.dashboard()
      ?.pendingDemands ?? 0;

  }


  get acceptedDemands(): number {

    return this.dashboard()
      ?.acceptedDemands ?? 0;

  }


  get rejectedDemands(): number {

    return this.dashboard()
      ?.rejectedDemands ?? 0;

  }


  // =========================================================
  // TICKET COMPLETION RATE
  // =========================================================

  get ticketCompletionRate(): number {

    const total =
      this.totalTickets;


    if (!total) {

      return 0;

    }


    return Math.round(
      (
        this.completedTickets /
        total
      ) * 100
    );

  }


  // =========================================================
  // DEMAND ACCEPTANCE RATE
  // =========================================================

  get demandAcceptanceRate(): number {

    const total =
      this.totalDemands;


    if (!total) {

      return 0;

    }


    return Math.round(
      (
        this.acceptedDemands /
        total
      ) * 100
    );

  }

}
