import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  QueryList,
  ViewChildren,
  inject,
  signal
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';
import {ClientService} from '../../services/client-services/client.service';
import {ClientDashboardModel} from '../models/client-dashboard.model';




Chart.register(
  ...registerables
);


@Component({
  selector: 'app-client-dashboard',
  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],

  templateUrl:
    './client-dashboard.html',

  styleUrl:
    './client-dashboard.css',

  changeDetection:
  ChangeDetectionStrategy.OnPush
})
export class ClientDashboard
  implements AfterViewInit, OnDestroy {


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
  // STATE
  // =========================================================

  readonly dashboard =
    signal<ClientDashboardModel | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);


  // =========================================================
  // SCROLL SECTIONS
  // =========================================================

  @ViewChildren(
    'dashboardSection',
    {
      read: ElementRef
    }
  )
  private sections!: QueryList<
    ElementRef<HTMLElement>
  >;


  private observer?: IntersectionObserver;


  // =========================================================
  // CHARTS
  // =========================================================

  private charts: Chart[] = [];


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngAfterViewInit(): void {

    this.loadDashboard();

    this.initializeScrollAnimations();

  }


  ngOnDestroy(): void {

    this.observer?.disconnect();

    this.destroyCharts();

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD DASHBOARD
  // =========================================================

  private loadDashboard(): void {

    const token =
      this.keycloak.tokenParsed;

    const clientEmail =
      token?.['email'] as string | undefined;


    if (
      !clientEmail ||
      !clientEmail.trim()
    ) {

      this.loading.set(false);

      this.error.set(true);

      this.cdr.markForCheck();

      return;

    }


    this.loading.set(true);

    this.error.set(false);


    this.clientService
      .getDashboard(
        clientEmail.trim()
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

          this.error.set(false);

          this.cdr.markForCheck();


          setTimeout(() => {

            this.createCharts();

            this.initializeScrollAnimations();

          });

        },


        error: error => {

          console.error(
            'Unable to load client dashboard:',
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
  // CHART CREATION
  // =========================================================

  private createCharts(): void {

    const data =
      this.dashboard();


    if (!data) {

      return;

    }


    this.destroyCharts();


    const buyingCanvas =
      document.getElementById(
        'buyingChart'
      ) as HTMLCanvasElement | null;


    const ticketCanvas =
      document.getElementById(
        'ticketChart'
      ) as HTMLCanvasElement | null;


    const subscriptionCanvas =
      document.getElementById(
        'subscriptionChart'
      ) as HTMLCanvasElement | null;


    if (
      !buyingCanvas ||
      !ticketCanvas ||
      !subscriptionCanvas
    ) {

      return;

    }


    // =======================================================
    // BUYINGS
    // =======================================================

    const buyingConfig:
      ChartConfiguration<'doughnut'> = {

      type:
        'doughnut',

      data: {

        labels: [
          'Active',
          'Other'
        ],

        datasets: [
          {

            data: [

              data.activeBuyings,

              Math.max(
                data.totalBuyings -
                data.activeBuyings,
                0
              )

            ],

            backgroundColor: [
              '#6366f1',
              '#e2e8f0'
            ],

            borderWidth:
              0,

            hoverOffset:
              7

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

        animation: {

          duration:
            1100,

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

              padding:
                16,

              font: {

                size:
                  11

              }

            }

          }

        }

      }

    };


    // =======================================================
    // TICKETS
    // =======================================================

    const ticketConfig:
      ChartConfiguration<'doughnut'> = {

      type:
        'doughnut',

      data: {

        labels: [
          'Pending',
          'Completed',
          'Other'
        ],

        datasets: [
          {

            data: [

              data.pendingTickets,

              data.completedTickets,

              Math.max(
                data.totalTickets -
                data.pendingTickets -
                data.completedTickets,
                0
              )

            ],

            backgroundColor: [
              '#f59e0b',
              '#22c55e',
              '#e2e8f0'
            ],

            borderWidth:
              0,

            hoverOffset:
              7

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

        animation: {

          duration:
            1200,

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

              padding:
                16,

              font: {

                size:
                  11

              }

            }

          }

        }

      }

    };


    // =======================================================
    // SUBSCRIPTION
    // =======================================================

    const subscriptionConfig:
      ChartConfiguration<'doughnut'> = {

      type:
        'doughnut',

      data: {

        labels: [
          'Subscribed',
          'Not subscribed'
        ],

        datasets: [
          {

            data: [
              data.subscribed ? 1 : 0,
              data.subscribed ? 0 : 1
            ],

            backgroundColor: [
              '#8b5cf6',
              '#e2e8f0'
            ],

            borderWidth:
              0,

            hoverOffset:
              7

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

        animation: {

          duration:
            1000,

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

              padding:
                16,

              font: {

                size:
                  11

              }

            }

          }

        }

      }

    };


    this.charts = [

      new Chart(
        buyingCanvas,
        buyingConfig
      ),

      new Chart(
        ticketCanvas,
        ticketConfig
      ),

      new Chart(
        subscriptionCanvas,
        subscriptionConfig
      )

    ];

  }


  // =========================================================
  // DESTROY CHARTS
  // =========================================================

  private destroyCharts(): void {

    this.charts.forEach(
      chart => chart.destroy()
    );

    this.charts = [];

  }


  // =========================================================
  // SCROLL ANIMATION
  // =========================================================

  private initializeScrollAnimations(): void {

    if (
      !this.sections
    ) {

      return;

    }


    if (
      typeof IntersectionObserver ===
      'undefined'
    ) {

      this.sections.forEach(
        section => {

          section.nativeElement
            .classList
            .add('is-visible');

        }
      );

      return;

    }


    this.observer?.disconnect();


    this.observer =
      new IntersectionObserver(

        entries => {

          entries.forEach(
            entry => {

              const element =
                entry.target as HTMLElement;


              if (
                entry.isIntersecting
              ) {

                element.classList.add(
                  'is-visible'
                );

              }

            }
          );

        },

        {
          threshold:
            0.12,

          rootMargin:
            '-60px 0px -60px 0px'
        }

      );


    this.sections.forEach(
      section => {

        this.observer?.observe(
          section.nativeElement
        );

      }
    );

  }

}
