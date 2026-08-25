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
import Keycloak from 'keycloak-js';

import {
  Chart,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  DoughnutController,
  BarController
} from 'chart.js';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { ClientService } from '../../../services/client-services/client.service';


// =========================================================
// CHART.JS REGISTRATION
// =========================================================

Chart.register(
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  DoughnutController,
  BarController
);


@Component({
  selector: 'app-client-stats',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './client-stats.html',

  styleUrl: './client-stats.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientStats
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly clientService =
    inject(ClientService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // VIEW CHILDREN
  // =========================================================

  @ViewChild('activityChart')
  activityChart!: ElementRef<HTMLCanvasElement>;

  @ViewChild('distributionChart')
  distributionChart!: ElementRef<HTMLCanvasElement>;


  // =========================================================
  // CHART INSTANCES
  // =========================================================

  private activityChartInstance?: Chart;

  private distributionChartInstance?: Chart;


  // =========================================================
  // STATE
  // =========================================================

  loading = true;

  error = false;

  email = '';

  dashboard: any = null;


  // =========================================================
  // REAL DASHBOARD VALUES
  // =========================================================

  budget = 0;

  totalBuyings = 0;

  activeBuyings = 0;

  totalTickets = 0;

  pendingTickets = 0;

  completedTickets = 0;

  subscribed = false;

  subscriptionType = 'None';


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadClientEmail();

  }


  ngAfterViewInit(): void {

    /*
     * Do not create charts here.
     *
     * The dashboard data arrives asynchronously.
     * Charts are created after the API response.
     */

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

    this.destroyCharts();

  }


  // =========================================================
  // GET KEYCLOAK EMAIL
  // =========================================================

  private loadClientEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;


    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.loading = false;

      this.error = true;

      this.cdr.markForCheck();

      return;
    }


    this.email = email;

    this.loadDashboard();

  }


  // =========================================================
  // LOAD DASHBOARD
  // =========================================================

  loadDashboard(): void {

    if (!this.email) {
      return;
    }


    this.loading = true;

    this.error = false;


    this.clientService
      .getDashboard(this.email)

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: dashboard => {

          console.log(
            'CLIENT DASHBOARD:',
            dashboard
          );


          this.dashboard =
            dashboard;


          this.extractStatistics(
            dashboard
          );


          this.cdr.markForCheck();


          /*
           * Important:
           *
           * Wait until Angular has updated the DOM.
           * Then create the charts.
           */

          setTimeout(() => {

            this.createCharts();

          }, 250);

        },


        error: error => {

          console.error(
            'Unable to load client dashboard:',
            error
          );


          this.error = true;

          this.dashboard = null;

          this.resetStatistics();

          this.destroyCharts();

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // EXTRACT DATA
  // =========================================================

  private extractStatistics(
    dashboard: any
  ): void {

    if (!dashboard) {

      this.resetStatistics();

      return;

    }


    this.budget =
      Number(
        dashboard.budget ?? 0
      );


    this.totalBuyings =
      Number(
        dashboard.totalBuyings ?? 0
      );


    this.activeBuyings =
      Number(
        dashboard.activeBuyings ?? 0
      );


    this.totalTickets =
      Number(
        dashboard.totalTickets ?? 0
      );


    this.pendingTickets =
      Number(
        dashboard.pendingTickets ?? 0
      );


    this.completedTickets =
      Number(
        dashboard.completedTickets ?? 0
      );


    this.subscribed =
      Boolean(
        dashboard.subscribed
      );


    this.subscriptionType =
      dashboard.subscriptionType ||
      'None';

  }


  // =========================================================
  // RESET
  // =========================================================

  private resetStatistics(): void {

    this.budget = 0;

    this.totalBuyings = 0;

    this.activeBuyings = 0;

    this.totalTickets = 0;

    this.pendingTickets = 0;

    this.completedTickets = 0;

    this.subscribed = false;

    this.subscriptionType = 'None';

  }


  // =========================================================
  // CREATE CHARTS
  // =========================================================

  private createCharts(): void {

    /*
     * Check that the canvases actually exist.
     */

    if (
      !this.activityChart ||
      !this.distributionChart
    ) {

      console.warn(
        'Chart canvas elements are not available yet.'
      );

      return;

    }


    const activityCanvas =
      this.activityChart.nativeElement;

    const distributionCanvas =
      this.distributionChart.nativeElement;


    /*
     * Check canvas contexts.
     */

    const activityContext =
      activityCanvas.getContext('2d');

    const distributionContext =
      distributionCanvas.getContext('2d');


    if (
      !activityContext ||
      !distributionContext
    ) {

      console.error(
        'Unable to get Chart.js canvas context.'
      );

      return;

    }


    /*
     * Remove old charts first.
     */

    this.destroyCharts();


    this.createActivityChart(
      activityContext
    );


    this.createDistributionChart(
      distributionContext
    );

  }


  // =========================================================
  // ACTIVITY BAR CHART
  // =========================================================

  private createActivityChart(
    context: CanvasRenderingContext2D
  ): void {

    this.activityChartInstance =
      new Chart(context, {

        type: 'bar',

        data: {

          labels: [
            'Buyings',
            'Active buyings',
            'Tickets',
            'Pending',
            'Completed'
          ],

          datasets: [

            {

              label: 'Activity',

              data: [

                this.totalBuyings,

                this.activeBuyings,

                this.totalTickets,

                this.pendingTickets,

                this.completedTickets

              ],

              borderRadius: 12,

              borderSkipped: false,

              backgroundColor: [

                '#6366f1',

                '#8b5cf6',

                '#06b6d4',

                '#f59e0b',

                '#10b981'

              ],

              hoverBackgroundColor: [

                '#818cf8',

                '#a78bfa',

                '#22d3ee',

                '#fbbf24',

                '#34d399'

              ],

              maxBarThickness: 52

            }

          ]

        },


        options: {

          responsive: true,

          maintainAspectRatio: false,


          animation: {

            duration: 1200,

            easing: 'easeOutQuart'

          },


          plugins: {

            legend: {

              display: false

            },


            tooltip: {

              backgroundColor: '#111827',

              titleColor: '#ffffff',

              bodyColor: '#e5e7eb',

              padding: 14,

              cornerRadius: 12,

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

                  size: 12,

                  weight: 500

                }

              }

            },


            y: {

              beginAtZero: true,

              suggestedMax: Math.max(
                5,
                this.totalBuyings,
                this.totalTickets
              ) + 1,

              grid: {

                color:
                  'rgba(148,163,184,0.12)'

              },

              border: {

                display: false

              },

              ticks: {

                color: '#64748b',

                precision: 0

              }

            }

          }

        }

      });

  }


  // =========================================================
  // DISTRIBUTION CHART
  // =========================================================

  private createDistributionChart(
    context: CanvasRenderingContext2D
  ): void {

    this.distributionChartInstance =
      new Chart(context, {

        type: 'doughnut',

        data: {

          labels: [

            'Active buyings',

            'Pending tickets',

            'Completed tickets'

          ],


          datasets: [

            {

              data: [

                this.activeBuyings,

                this.pendingTickets,

                this.completedTickets

              ],


              backgroundColor: [

                '#8b5cf6',

                '#f59e0b',

                '#10b981'

              ],


              hoverBackgroundColor: [

                '#a78bfa',

                '#fbbf24',

                '#34d399'

              ],


              borderWidth: 0,

              hoverOffset: 10

            }

          ]

        },


        options: {

          responsive: true,

          maintainAspectRatio: false,

          cutout: '72%',


          animation: {

            animateRotate: true,

            animateScale: true,

            duration: 1400

          },


          plugins: {

            legend: {

              position: 'bottom',

              labels: {

                padding: 18,

                usePointStyle: true,

                pointStyle: 'circle',

                color: '#475569',

                font: {

                  size: 12,

                  weight: 500

                }

              }

            },


            tooltip: {

              backgroundColor: '#111827',

              padding: 14,

              cornerRadius: 12

            }

          }

        }

      });

  }


  // =========================================================
  // DESTROY CHARTS
  // =========================================================

  private destroyCharts(): void {

    if (this.activityChartInstance) {

      this.activityChartInstance.destroy();

      this.activityChartInstance =
        undefined;

    }


    if (this.distributionChartInstance) {

      this.distributionChartInstance.destroy();

      this.distributionChartInstance =
        undefined;

    }

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading) {
      return;
    }

    this.loadDashboard();

  }


  // =========================================================
  // HELPERS
  // =========================================================

  formatMoney(
    value: number
  ): string {

    return new Intl.NumberFormat(
      'en-US',
      {

        style: 'currency',

        currency: 'EUR',

        maximumFractionDigits: 2

      }
    ).format(value);

  }


  getActivityTotal(): number {

    return (
      this.totalBuyings +
      this.totalTickets
    );

  }


  getTicketCompletionRate(): number {

    if (this.totalTickets === 0) {
      return 0;
    }

    return Math.round(
      (
        this.completedTickets /
        this.totalTickets
      ) * 100
    );

  }

}
