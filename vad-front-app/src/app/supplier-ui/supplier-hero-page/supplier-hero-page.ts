import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  QueryList,
  ViewChildren,
  afterNextRender,
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

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';


import {retry} from 'rxjs';
import {SupplierDetails} from '../models/supplier-details.model';
import {SupplierDashboard} from '../models/supplier-dashboard.model';
import {SupplierDetailsService} from '../../services/supplier-services/supplier-details';

Chart.register(...registerables);

@Component({
  selector: 'app-supplier-hero-page',
  standalone: true,

  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],

  templateUrl: './supplier-hero-page.html',
  styleUrl: './supplier-hero-page.css'
})
export class SupplierHeroPage
  implements OnInit, AfterViewInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);
  private readonly supplierService = inject(SupplierDetailsService);

  @ViewChildren('revealElement', {
    read: ElementRef
  })
  private revealElements!: QueryList<ElementRef>;

  readonly supplier = signal<SupplierDetails | null>(null);

  readonly dashboard = signal<SupplierDashboard | null>(null);

  readonly loading = signal(true);

  readonly error = signal(false);

  readonly email = signal('');

  private observer?: IntersectionObserver;

  private charts: Chart[] = [];

  ngOnInit(): void {
    this.loadSupplier();
  }

  ngAfterViewInit(): void {
    this.initializeScrollReveal();

    this.revealElements.changes.subscribe(() => {
      setTimeout(() => {
        this.initializeScrollReveal();
      });
    });
  }

  ngOnDestroy(): void {
    this.destroyCharts();

    this.observer?.disconnect();
  }

  // ---------------------------------------------------------
  // DATA
  // ---------------------------------------------------------

  private loadSupplier(): void {

    const token = this.keycloak.tokenParsed;

    const supplierEmail =
      token?.['email'] ?? '';

    if (!supplierEmail) {
      this.error.set(true);
      this.loading.set(false);
      return;
    }

    this.email.set(supplierEmail);

    this.loading.set(true);

    this.supplierService
      .getSupplierDetails(supplierEmail)
      .subscribe({
        next: (details) => {
          this.supplier.set(details);
        },

        error: (error) => {
          console.error(
            'Unable to load supplier details',
            error
          );

          this.error.set(true);
        }
      });

    this.supplierService
      .getSupplierDashboard(supplierEmail)
      .subscribe({
        next: (dashboard) => {
          this.dashboard.set(dashboard);

          this.loading.set(false);

          /*
           * Charts are created after the dashboard
           * data arrives.
           */
          requestAnimationFrame(() => {
            requestAnimationFrame(() => {
              this.createCharts();
            });
          });
        },

        error: (error) => {
          console.error(
            'Unable to load supplier dashboard',
            error
          );

          this.error.set(true);
          this.loading.set(false);
        }
      });
  }

  // ---------------------------------------------------------
  // SCROLL REVEAL
  // ---------------------------------------------------------

  private initializeScrollReveal(): void {

    if (!this.revealElements?.length) {
      return;
    }

    this.observer?.disconnect();

    this.observer = new IntersectionObserver(
      (entries) => {

        entries.forEach(entry => {

          const element = entry.target as HTMLElement;

          if (entry.isIntersecting) {
            element.classList.add('is-visible');
          } else {
            element.classList.remove('is-visible');
          }

        });

      },
      {
        threshold: 0.05,
        rootMargin: '0px 0px -5% 0px'
      }
    );

    this.revealElements.forEach(element => {

      const nativeElement =
        element.nativeElement as HTMLElement;

      this.observer?.observe(nativeElement);

      /*
       * Important:
       * IntersectionObserver can sometimes wait for a
       * scroll event before reporting elements that are
       * already visible during the initial render.
       *
       * Check the element manually.
       */
      const rect =
        nativeElement.getBoundingClientRect();

      const viewportHeight =
        window.innerHeight ||
        document.documentElement.clientHeight;

      const alreadyVisible =
        rect.top < viewportHeight &&
        rect.bottom > 0;

      if (alreadyVisible) {
        nativeElement.classList.add('is-visible');
      }

    });
  }

  // ---------------------------------------------------------
  // CHARTS
  // ---------------------------------------------------------

  private createCharts(): void {

    const data = this.dashboard();

    if (!data) {
      return;
    }

    /*
     * If Angular/@defer has not rendered the canvases yet,
     * wait for the next render frame instead of silently
     * giving up.
     */
    const resourceCanvas =
      document.getElementById('resourceChart');

    const demandCanvas =
      document.getElementById('demandChart');

    const activityCanvas =
      document.getElementById('activityChart');

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

    this.createResourceChart(data);

    this.createDemandChart(data);

    this.createActivityChart(data);
  }

  private createResourceChart(
    data: SupplierDashboard
  ): void {

    const canvas =
      document.getElementById(
        'resourceChart'
      ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

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
            label: 'Total',

            data: [
              data.totalVehicles,
              data.totalBuyings,
              data.totalSubscriptions
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

        plugins: {

          legend: {
            display: false
          },

          tooltip: {
            displayColors: false,

            callbacks: {
              label: context =>
                ` ${context.parsed.y} items`
            }
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
      new Chart(canvas, configuration)
    );
  }

  private createDemandChart(
    data: SupplierDashboard
  ): void {

    const canvas =
      document.getElementById(
        'demandChart'
      ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

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
              data.approvedDemands,
              data.pendingDemands,
              data.refusedDemands
            ],

            borderWidth: 0,

            spacing: 4,

            hoverOffset: 8
          }
        ]
      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        cutout: '70%',

        plugins: {

          legend: {
            position: 'bottom',

            labels: {
              usePointStyle: true,

              padding: 20
            }
          },

          tooltip: {
            callbacks: {
              label: context =>
                ` ${context.label}: ${context.parsed}`
            }
          }
        }
      }
    };

    this.charts.push(
      new Chart(canvas, configuration)
    );
  }

  private createActivityChart(
    data: SupplierDashboard
  ): void {

    const canvas =
      document.getElementById(
        'activityChart'
      ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

    const activePercentage =
      data.totalBuyings > 0
        ? Math.round(
          (data.activeBuyings /
            data.totalBuyings) * 100
        )
        : 0;

    const inactivePercentage =
      Math.max(
        0,
        100 - activePercentage
      );

    const configuration:
      ChartConfiguration<'doughnut'> = {

      type: 'doughnut',

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

            borderWidth: 0,

            spacing: 5,

            hoverOffset: 10
          }
        ]
      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        cutout: '76%',

        plugins: {

          legend: {
            position: 'bottom',

            labels: {
              usePointStyle: true,

              padding: 20
            }
          },

          tooltip: {

            callbacks: {

              label: context =>
                ` ${context.label}: ${context.parsed}%`
            }
          }
        }
      }
    };

    this.charts.push(
      new Chart(canvas, configuration)
    );
  }

  private destroyCharts(): void {

    this.charts.forEach(
      chart => chart.destroy()
    );

    this.charts = [];
  }

  protected readonly retry = retry;
}
