import {
  AfterViewInit,
  Component,
  OnDestroy,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import {
  MatCardModule
} from '@angular/material/card';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  SupplierDashboard as SupplierDashboardModel
} from '../models/supplier-dashboard.model';

import {
  SupplierDetailsService
} from '../../services/supplier-services/supplier-details';


Chart.register(
  ...registerables
);


@Component({

  selector:
    'app-supplier-dashboard',

  standalone:
    true,

  imports: [

    CommonModule,

    MatCardModule,

    MatIconModule,

    MatProgressSpinnerModule

  ],

  templateUrl:
    './supplier-dashboard.html',

  styleUrl:
    './supplier-dashboard.css'

})
export class SupplierDashboard
  implements AfterViewInit, OnDestroy {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly supplierService =
    inject(SupplierDetailsService);


  // =========================================================
  // STATE
  // =========================================================

  dashboard:
    SupplierDashboardModel | null = null;

  loading = true;

  error = false;


  // =========================================================
  // CHARTS
  // =========================================================

  private charts:
    Chart[] = [];


  private chartsInitialized =
    false;


  // =========================================================
  // INIT
  // =========================================================

  ngAfterViewInit(): void {

    this.loadDashboard();

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    this.destroyCharts();

  }


  // =========================================================
  // LOAD DASHBOARD
  // =========================================================

  private loadDashboard(): void {

    const token =
      this.keycloak.tokenParsed;


    const supplierEmail =
      token?.['email'] ?? '';


    if (!supplierEmail) {

      this.error = true;

      this.loading = false;

      return;

    }


    this.loading = true;


    this.supplierService

      .getSupplierDashboard(
        supplierEmail
      )

      .subscribe({

        next: dashboard => {

          this.dashboard =
            dashboard;

          this.loading =
            false;

          /*
           * Wait for Angular to render
           * the dashboard canvases.
           */

          requestAnimationFrame(() => {

            requestAnimationFrame(() => {

              this.createCharts();

            });

          });

        },

        error: error => {

          console.error(
            'Unable to load supplier dashboard:',
            error
          );

          this.error =
            true;

          this.loading =
            false;

        }

      });

  }


  // =========================================================
  // CREATE CHARTS
  // =========================================================

  private createCharts(): void {

    if (
      !this.dashboard ||
      this.chartsInitialized
    ) {

      return;

    }


    const resourceCanvas =
      document.getElementById(
        'resourceChart'
      ) as HTMLCanvasElement | null;


    const demandCanvas =
      document.getElementById(
        'demandChart'
      ) as HTMLCanvasElement | null;


    const activityCanvas =
      document.getElementById(
        'activityChart'
      ) as HTMLCanvasElement | null;


    if (
      !resourceCanvas ||
      !demandCanvas ||
      !activityCanvas
    ) {

      requestAnimationFrame(() => {

        this.createCharts();

      });

      return;

    }


    this.destroyCharts();


    this.createResourceChart();

    this.createDemandChart();

    this.createActivityChart();


    this.chartsInitialized =
      true;

  }


  // =========================================================
  // RESOURCE CHART
  // =========================================================

  private createResourceChart(): void {

    if (!this.dashboard) {

      return;

    }


    const canvas =
      document.getElementById(
        'resourceChart'
      ) as HTMLCanvasElement;


    const configuration:
      ChartConfiguration<'bar'> = {

      type: 'bar',

      data: {

        labels: [
          'Vehicles',
          'Buyings',
          'Subscriptions'
        ],

        datasets: [

          {

            label:
              'Total',

            data: [

              this.dashboard.totalVehicles,

              this.dashboard.totalBuyings,

              this.dashboard.totalSubscriptions

            ],

            borderRadius:
              10,

            borderSkipped:
              false,

            barThickness:
              42

          }

        ]

      },

      options: {

        responsive:
          true,

        maintainAspectRatio:
          false,

        plugins: {

          legend: {

            display:
              false

          },

          tooltip: {

            displayColors:
              false,

            callbacks: {

              label:
                context =>
                  ` ${context.parsed.y} items`

            }

          }

        },

        scales: {

          x: {

            grid: {

              display:
                false

            }

          },

          y: {

            beginAtZero:
              true,

            ticks: {

              precision:
                0

            },

            grid: {

              color:
                'rgba(0,0,0,0.06)'

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


  // =========================================================
  // DEMAND CHART
  // =========================================================

  private createDemandChart(): void {

    if (!this.dashboard) {

      return;

    }


    const canvas =
      document.getElementById(
        'demandChart'
      ) as HTMLCanvasElement;


    const configuration:
      ChartConfiguration<'doughnut'> = {

      type: 'doughnut',

      data: {

        labels: [

          'Approved',

          'Pending',

          'Refused'

        ],

        datasets: [

          {

            data: [

              this.dashboard.approvedDemands,

              this.dashboard.pendingDemands,

              this.dashboard.refusedDemands

            ],

            borderWidth:
              0,

            spacing:
              4,

            hoverOffset:
              8

          }

        ]

      },

      options: {

        responsive:
          true,

        maintainAspectRatio:
          false,

        cutout:
          '70%',

        plugins: {

          legend: {

            position:
              'bottom',

            labels: {

              usePointStyle:
                true,

              padding:
                20

            }

          },

          tooltip: {

            callbacks: {

              label:
                context =>
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


  // =========================================================
  // ACTIVITY CHART
  // =========================================================

  private createActivityChart(): void {

    if (!this.dashboard) {

      return;

    }


    const canvas =
      document.getElementById(
        'activityChart'
      ) as HTMLCanvasElement;


    const activePercentage =

      this.dashboard.totalBuyings > 0

        ? Math.round(

          (
            this.dashboard.activeBuyings /
            this.dashboard.totalBuyings
          ) * 100

        )

        : 0;


    const inactivePercentage =

      Math.max(
        0,
        100 - activePercentage
      );


    const configuration:
      ChartConfiguration<'doughnut'> = {

      type:
        'doughnut',

      data: {

        labels: [

          'Active',

          'Inactive'

        ],

        datasets: [

          {

            data: [

              activePercentage,

              inactivePercentage

            ],

            borderWidth:
              0,

            spacing:
              5,

            hoverOffset:
              10

          }

        ]

      },

      options: {

        responsive:
          true,

        maintainAspectRatio:
          false,

        cutout:
          '76%',

        plugins: {

          legend: {

            position:
              'bottom',

            labels: {

              usePointStyle:
                true,

              padding:
                20

            }

          },

          tooltip: {

            callbacks: {

              label:
                context =>
                  ` ${context.label}: ${context.parsed}%`

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


  // =========================================================
  // DESTROY CHARTS
  // =========================================================

  private destroyCharts(): void {

    this.charts.forEach(
      chart =>
        chart.destroy()
    );

    this.charts = [];

    this.chartsInitialized =
      false;

  }

}
