import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';


import { AdminDashboardModel } from '../models/admin-dashboard.model';
import {AdminDetailsService} from '../../services/admin-services/admin-details.service';


Chart.register(...registerables);


@Component({
  selector: 'app-admin-dashboard',
  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminDashboard
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly dashboardService =
    inject(AdminDetailsService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // CHART REFERENCES
  // =========================================================

  @ViewChild('overviewChart')
  overviewChartRef!: ElementRef<HTMLCanvasElement>;

  @ViewChild('repairChart')
  repairChartRef!: ElementRef<HTMLCanvasElement>;

  @ViewChild('operationsChart')
  operationsChartRef!: ElementRef<HTMLCanvasElement>;


  // =========================================================
  // CHART INSTANCES
  // =========================================================

  private overviewChart?: Chart;

  private repairChart?: Chart;

  private operationsChart?: Chart;


  // =========================================================
  // DATA
  // =========================================================

  dashboard: AdminDashboardModel | null = null;

  chartsReady = false;


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadDashboard();

  }


  ngAfterViewInit(): void {

    if (this.dashboard) {

      this.createCharts();

    }

  }


  ngOnDestroy(): void {

    this.destroyCharts();

  }


  // =========================================================
  // LOAD DASHBOARD
  // =========================================================

  private loadDashboard(): void {

    this.dashboardService
      .getDashboard()
      .subscribe({

        next: (data) => {

          this.dashboard = data;

          this.cdr.markForCheck();

          setTimeout(() => {

            this.createCharts();

          });

        },

        error: (error) => {

          console.error(
            'Unable to load admin dashboard:',
            error
          );

        }

      });

  }


  // =========================================================
  // CREATE CHARTS
  // =========================================================

  private createCharts(): void {

    if (!this.dashboard) {

      return;

    }

    if (
      !this.overviewChartRef ||
      !this.repairChartRef ||
      !this.operationsChartRef
    ) {

      return;

    }


    this.destroyCharts();


    this.createOverviewChart();

    this.createRepairChart();

    this.createOperationsChart();


    this.chartsReady = true;

    this.cdr.markForCheck();

  }


  // =========================================================
  // OVERVIEW CHART
  // =========================================================

  private createOverviewChart(): void {

    if (!this.dashboard) {
      return;
    }


    const context =
      this.overviewChartRef.nativeElement.getContext('2d');


    if (!context) {
      return;
    }


    const data = {

      labels: [
        'Clients',
        'Suppliers',
        'Vehicles',
        'Locations',
        'Buyings',
        'Subscriptions',
        'Tickets',
        'Demands'
      ],

      datasets: [

        {

          label: 'Total',

          data: [

            this.dashboard.totalClients,

            this.dashboard.totalSuppliers,

            this.dashboard.totalVehicles,

            this.dashboard.totalLocations,

            this.dashboard.totalBuyings,

            this.dashboard.totalSubscriptions,

            this.dashboard.totalTickets,

            this.dashboard.totalDemands

          ],

          borderRadius: 8,

          borderWidth: 0

        }

      ]

    };


    const config:
      ChartConfiguration<'bar'> = {

      type: 'bar',

      data,

      options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {
            display: false
          },

          tooltip: {

            backgroundColor: '#111827',

            padding: 12,

            titleFont: {
              size: 13,
              weight: 'bold'
            },

            bodyFont: {
              size: 12
            },

            displayColors: false

          }

        },

        scales: {

          x: {

            grid: {
              display: false
            },

            border: {
              display: false
            },

            ticks: {

              color: '#64748b',

              font: {
                size: 11,
                weight: 600
              }

            }

          },

          y: {

            beginAtZero: true,

            grid: {

              color: '#eef2f7'

            },

            border: {
              display: false
            },

            ticks: {

              color: '#94a3b8',

              precision: 0,

              font: {
                size: 11
              }

            }

          }

        }

      }

    };


    this.overviewChart =
      new Chart(
        context,
        config
      );

  }


  // =========================================================
  // REPAIR CHART
  // =========================================================

  private createRepairChart(): void {

    if (!this.dashboard) {
      return;
    }


    const context =
      this.repairChartRef.nativeElement.getContext('2d');


    if (!context) {
      return;
    }


    const active =
      this.dashboard.activeRepairs ?? 0;


    const total =
      this.dashboard.totalRepairs ?? 0;


    const inactive =
      Math.max(
        total - active,
        0
      );


    const data = {

      labels: [
        'Active repairs',
        'Other repairs'
      ],

      datasets: [

        {

          data: [
            active,
            inactive
          ],

          borderWidth: 0,

          hoverOffset: 5

        }

      ]

    };


    const config:
      ChartConfiguration<'doughnut'> = {

      type: 'doughnut',

      data,

      options: {

        responsive: true,

        maintainAspectRatio: false,

        cutout: '72%',

        plugins: {

          legend: {

            position: 'bottom',

            labels: {

              padding: 18,

              color: '#64748b',

              font: {

                size: 11,

                weight: 600

              },

              usePointStyle: true,

              pointStyle: 'circle'

            }

          },

          tooltip: {

            backgroundColor: '#111827',

            padding: 12,

            displayColors: false

          }

        }

      }

    };


    this.repairChart =
      new Chart(
        context,
        config
      );

  }


  // =========================================================
  // OPERATIONS CHART
  // =========================================================

  private createOperationsChart(): void {

    if (!this.dashboard) {
      return;
    }


    const context =
      this.operationsChartRef.nativeElement.getContext('2d');


    if (!context) {
      return;
    }


    const data = {

      labels: [

        'Repairs',

        'Vehicles',

        'Buyings',

        'Subscriptions',

        'Tickets',

        'Demands'

      ],

      datasets: [

        {

          label: 'Records',

          data: [

            this.dashboard.totalRepairs,

            this.dashboard.totalVehicles,

            this.dashboard.totalBuyings,

            this.dashboard.totalSubscriptions,

            this.dashboard.totalTickets,

            this.dashboard.totalDemands

          ],

          borderRadius: 8,

          borderWidth: 0

        }

      ]

    };


    const config:
      ChartConfiguration<'bar'> = {

      type: 'bar',

      data,

      options: {

        indexAxis: 'y',

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {
            display: false
          },

          tooltip: {

            backgroundColor: '#111827',

            padding: 12,

            displayColors: false

          }

        },

        scales: {

          x: {

            beginAtZero: true,

            grid: {

              color: '#eef2f7'

            },

            border: {
              display: false
            },

            ticks: {

              precision: 0,

              color: '#94a3b8',

              font: {
                size: 11
              }

            }

          },

          y: {

            grid: {
              display: false
            },

            border: {
              display: false
            },

            ticks: {

              color: '#64748b',

              font: {

                size: 11,

                weight: 600

              }

            }

          }

        }

      }

    };


    this.operationsChart =
      new Chart(
        context,
        config
      );

  }


  // =========================================================
  // DESTROY CHARTS
  // =========================================================

  private destroyCharts(): void {

    this.overviewChart?.destroy();

    this.repairChart?.destroy();

    this.operationsChart?.destroy();


    this.overviewChart = undefined;

    this.repairChart = undefined;

    this.operationsChart = undefined;

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    this.destroyCharts();

    this.chartsReady = false;

    this.loadDashboard();

  }


  // =========================================================
  // HELPERS
  // =========================================================

  getRepairPercentage(): number {

    if (!this.dashboard?.totalRepairs) {

      return 0;

    }


    return Math.round(

      (
        this.dashboard.activeRepairs /
        this.dashboard.totalRepairs
      ) * 100

    );

  }


  getTotalRecords(): number {

    if (!this.dashboard) {

      return 0;

    }


    return (

      (this.dashboard.totalClients ?? 0) +

      (this.dashboard.totalSuppliers ?? 0) +

      (this.dashboard.totalRepairs ?? 0) +

      (this.dashboard.totalLocations ?? 0) +

      (this.dashboard.totalVehicles ?? 0) +

      (this.dashboard.totalBuyings ?? 0) +

      (this.dashboard.totalSubscriptions ?? 0) +

      (this.dashboard.totalTickets ?? 0) +

      (this.dashboard.totalDemands ?? 0)

    );

  }

}
