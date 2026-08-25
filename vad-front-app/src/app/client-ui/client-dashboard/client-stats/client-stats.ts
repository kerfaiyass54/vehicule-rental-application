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

import { Subject, finalize, takeUntil } from 'rxjs';
import {ClientService} from '../../../services/client-services/client.service';


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

  private readonly keycloak = inject(Keycloak);

  private readonly clientService =
    inject(ClientService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly elementRef =
    inject(ElementRef);

  private readonly destroy$ =
    new Subject<void>();

  // ---------------------------------------------------------
  // CHART REFERENCES
  // ---------------------------------------------------------

  @ViewChild('activityChart')
  activityChart!: ElementRef<HTMLCanvasElement>;

  @ViewChild('distributionChart')
  distributionChart!: ElementRef<HTMLCanvasElement>;

  private activityChartInstance?: Chart;

  private distributionChartInstance?: Chart;


  // ---------------------------------------------------------
  // STATE
  // ---------------------------------------------------------

  loading = true;

  error = false;

  email = '';

  dashboard: any = null;


  // ---------------------------------------------------------
  // STATISTICS
  // ---------------------------------------------------------

  totalBuyings = 0;

  totalSubscriptions = 0;

  totalTickets = 0;

  totalVehicles = 0;

  totalSpent = 0;

  activeSubscriptions = 0;

  pendingTickets = 0;

  completedTickets = 0;


  // ---------------------------------------------------------
  // LIFECYCLE
  // ---------------------------------------------------------

  ngOnInit(): void {

    this.loadClientEmail();

  }


  ngAfterViewInit(): void {

    this.setupScrollAnimations();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

    this.destroyCharts();

  }


  // ---------------------------------------------------------
  // KEYCLOAK
  // ---------------------------------------------------------

  private loadClientEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const clientEmail =
      token?.['email'];

    if (!clientEmail) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.error = true;

      this.loading = false;

      this.cdr.markForCheck();

      return;
    }

    this.email = clientEmail;

    this.loadDashboard();

  }


  // ---------------------------------------------------------
  // DASHBOARD
  // ---------------------------------------------------------

  loadDashboard(): void {

    if (!this.email) {
      return;
    }

    this.loading = true;

    this.error = false;

    this.cdr.markForCheck();

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

          this.dashboard = dashboard;

          this.extractStatistics(dashboard);

          this.cdr.markForCheck();

          setTimeout(() => {

            this.createCharts();

          }, 100);

        },

        error: error => {

          console.error(
            'Unable to load client dashboard',
            error
          );

          this.error = true;

          this.dashboard = null;

          this.resetStatistics();

          this.cdr.markForCheck();

        }

      });

  }


  // ---------------------------------------------------------
  // EXTRACT STATISTICS
  // ---------------------------------------------------------

  private extractStatistics(
    dashboard: any
  ): void {

    if (!dashboard) {
      this.resetStatistics();
      return;
    }

    /*
     * The helper allows the component to work with
     * slightly different DTO naming conventions.
     */

    this.totalBuyings =
      this.readNumber(
        dashboard,
        'totalBuyings',
        'buyingsCount',
        'numberOfBuyings'
      );

    this.totalSubscriptions =
      this.readNumber(
        dashboard,
        'totalSubscriptions',
        'subscriptionsCount',
        'numberOfSubscriptions'
      );

    this.totalTickets =
      this.readNumber(
        dashboard,
        'totalTickets',
        'ticketsCount',
        'numberOfTickets'
      );

    this.totalVehicles =
      this.readNumber(
        dashboard,
        'totalVehicles',
        'vehiclesCount',
        'numberOfVehicles'
      );

    this.totalSpent =
      this.readNumber(
        dashboard,
        'totalSpent',
        'totalAmount',
        'amountSpent',
        'totalPrice'
      );

    this.activeSubscriptions =
      this.readNumber(
        dashboard,
        'activeSubscriptions',
        'activeSubscriptionCount'
      );

    this.pendingTickets =
      this.readNumber(
        dashboard,
        'pendingTickets',
        'pendingTicketCount'
      );

    this.completedTickets =
      this.readNumber(
        dashboard,
        'completedTickets',
        'completedTicketCount'
      );

  }


  // ---------------------------------------------------------
  // SAFE NUMBER READER
  // ---------------------------------------------------------

  private readNumber(
    object: any,
    ...keys: string[]
  ): number {

    for (const key of keys) {

      const value = object?.[key];

      if (
        value !== null &&
        value !== undefined &&
        !isNaN(Number(value))
      ) {

        return Number(value);

      }

    }

    return 0;

  }


  // ---------------------------------------------------------
  // RESET
  // ---------------------------------------------------------

  private resetStatistics(): void {

    this.totalBuyings = 0;

    this.totalSubscriptions = 0;

    this.totalTickets = 0;

    this.totalVehicles = 0;

    this.totalSpent = 0;

    this.activeSubscriptions = 0;

    this.pendingTickets = 0;

    this.completedTickets = 0;

  }


  // ---------------------------------------------------------
  // CHARTS
  // ---------------------------------------------------------

  private createCharts(): void {

    if (
      !this.activityChart ||
      !this.distributionChart
    ) {

      return;

    }

    this.destroyCharts();

    this.createActivityChart();

    this.createDistributionChart();

  }


  private createActivityChart(): void {

    const context =
      this.activityChart.nativeElement
        .getContext('2d');

    if (!context) {
      return;
    }

    this.activityChartInstance =
      new Chart(context, {

        type: 'bar',

        data: {

          labels: [
            'Buyings',
            'Subscriptions',
            'Tickets',
            'Vehicles'
          ],

          datasets: [

            {
              label: 'Activity',

              data: [
                this.totalBuyings,
                this.totalSubscriptions,
                this.totalTickets,
                this.totalVehicles
              ],

              borderRadius: 14,

              borderSkipped: false,

              barThickness: 42,

              backgroundColor: [
                '#6366f1',
                '#8b5cf6',
                '#06b6d4',
                '#10b981'
              ],

              hoverBackgroundColor: [
                '#818cf8',
                '#a78bfa',
                '#22d3ee',
                '#34d399'
              ]
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

              padding: 14,

              cornerRadius: 10,

              displayColors: false

            }

          },

          scales: {

            x: {

              grid: {
                display: false
              },

              ticks: {
                color: '#64748b'
              }

            },

            y: {

              beginAtZero: true,

              grid: {
                color: 'rgba(148,163,184,0.15)'
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


  private createDistributionChart(): void {

    const context =
      this.distributionChart.nativeElement
        .getContext('2d');

    if (!context) {
      return;
    }

    this.distributionChartInstance =
      new Chart(context, {

        type: 'doughnut',

        data: {

          labels: [
            'Buyings',
            'Subscriptions',
            'Tickets',
            'Vehicles'
          ],

          datasets: [

            {

              data: [
                this.totalBuyings,
                this.totalSubscriptions,
                this.totalTickets,
                this.totalVehicles
              ],

              backgroundColor: [
                '#6366f1',
                '#8b5cf6',
                '#06b6d4',
                '#10b981'
              ],

              hoverBackgroundColor: [
                '#818cf8',
                '#a78bfa',
                '#22d3ee',
                '#34d399'
              ],

              borderWidth: 0,

              hoverOffset: 8

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

                padding: 20,

                usePointStyle: true,

                pointStyle: 'circle',

                color: '#475569'

              }

            },

            tooltip: {

              backgroundColor: '#111827',

              padding: 14,

              cornerRadius: 10

            }

          }

        }

      });

  }


  // ---------------------------------------------------------
  // DESTROY CHARTS
  // ---------------------------------------------------------

  private destroyCharts(): void {

    this.activityChartInstance?.destroy();

    this.distributionChartInstance?.destroy();

    this.activityChartInstance = undefined;

    this.distributionChartInstance = undefined;

  }


  // ---------------------------------------------------------
  // REFRESH
  // ---------------------------------------------------------

  refresh(): void {

    if (this.loading) {
      return;
    }

    this.loadDashboard();

  }


  // ---------------------------------------------------------
  // SCROLL REVEAL
  // ---------------------------------------------------------

  private setupScrollAnimations(): void {

    const elements =
      this.elementRef.nativeElement
        .querySelectorAll('.reveal');

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

              observer.unobserve(
                entry.target
              );

            }

          });

        },

        {
          threshold: 0.12
        }

      );

    elements.forEach(
      (element: Element) =>
        observer.observe(element)
    );

  }


  // ---------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------

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
      this.totalSubscriptions +
      this.totalTickets +
      this.totalVehicles
    );

  }

}
