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
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatSelectModule
} from '@angular/material/select';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  forkJoin,
  takeUntil
} from 'rxjs';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import { RepairDetails as RepairDetailsService } from '../../services/repair-services/repair-details';

import { RepairProfile } from '../models/repair-profile.model';
import { Location } from '../models/location.model';
import { RepairDashboard } from '../models/repair-dashboard.model';


Chart.register(...registerables);


@Component({
  selector: 'app-repair-details',

  standalone: true,

  imports: [
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTooltipModule
  ],

  templateUrl: './repair-details.html',

  styleUrl: './repair-details.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RepairDetails implements OnInit, OnDestroy {


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

  readonly profile =
    signal<RepairProfile | null>(null);

  readonly locations =
    signal<Location[]>([]);

  readonly dashboard =
    signal<RepairDashboard | null>(null);

  readonly loading =
    signal(true);

  readonly updatingLocation =
    signal(false);

  readonly error =
    signal(false);

  readonly locationError =
    signal(false);

  readonly updateSuccess =
    signal(false);

  readonly repairEmail =
    signal('');

  readonly selectedLocationId =
    signal<number | null>(null);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

  }


  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

    this.destroyCharts();

  }


  // =========================================================
  // KEYCLOAK EMAIL
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    if (!email) {

      console.error(
        'Repair email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;

    }

    this.repairEmail.set(email);

    this.loadData();

  }


  // =========================================================
  // LOAD EVERYTHING
  // =========================================================

  loadData(): void {

    const email =
      this.repairEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.updateSuccess.set(false);

    this.cdr.markForCheck();


    forkJoin({

      profile:
        this.repairService.getInfo(email),

      locations:
        this.repairService.getLocations(),

      dashboard:
        this.repairService.getDashboard(email)

    })

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          this.profile.set(
            response.profile
          );

          this.locations.set(
            response.locations ?? []
          );

          this.dashboard.set(
            response.dashboard
          );


          // -----------------------------------------------------
          // SELECT CURRENT LOCATION
          // -----------------------------------------------------

          const currentLocation =
            response.locations.find(
              location =>
                location.name ===
                response.profile.locationName
            );

          if (currentLocation) {

            this.selectedLocationId.set(
              currentLocation.idLoc
            );

          }


          this.error.set(false);

          this.cdr.markForCheck();


          // Chart rendering happens after Angular updates
          // the view.
          setTimeout(() => {

            this.createCharts();

          });

        },


        error: error => {

          console.error(
            'Unable to load repair center details:',
            error
          );

          this.profile.set(null);
          this.dashboard.set(null);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading() ||
      this.updatingLocation()) {

      return;

    }

    this.destroyCharts();

    this.loadData();

  }


  // =========================================================
  // LOCATION SELECTION
  // =========================================================

  onLocationChange(
    locationId: number
  ): void {

    this.selectedLocationId.set(
      Number(locationId)
    );

    this.updateSuccess.set(false);

    this.locationError.set(false);

  }


  // =========================================================
  // UPDATE LOCATION
  // =========================================================

  updateLocation(): void {

    const email =
      this.repairEmail();

    const locationId =
      this.selectedLocationId();


    if (!email ||
      !locationId ||
      this.updatingLocation()) {

      return;

    }


    this.updatingLocation.set(true);

    this.locationError.set(false);

    this.updateSuccess.set(false);

    this.cdr.markForCheck();


    this.repairService
      .updateLocation(
        email,
        locationId
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.updatingLocation.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: updatedProfile => {

          this.profile.set(
            updatedProfile
          );

          this.updateSuccess.set(
            true
          );

          this.locationError.set(false);

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to update repair location:',
            error
          );

          this.locationError.set(
            true
          );

          this.updateSuccess.set(
            false
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // CHECK LOCATION CHANGE
  // =========================================================

  get locationChanged(): boolean {

    const currentProfile =
      this.profile();

    const selectedId =
      this.selectedLocationId();


    if (!currentProfile ||
      !selectedId) {

      return false;

    }


    const currentLocation =
      this.locations().find(
        location =>
          location.name ===
          currentProfile.locationName
      );


    return !currentLocation ||
      currentLocation.idLoc !== selectedId;

  }


  // =========================================================
  // GET SELECTED LOCATION
  // =========================================================

  get selectedLocation(): Location | null {

    const id =
      this.selectedLocationId();

    if (!id) {
      return null;
    }

    return (
      this.locations().find(
        location =>
          location.idLoc === id
      ) ?? null
    );

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

    this.createTicketChart(data);

    this.createDemandChart(data);

  }


  // =========================================================
  // TICKET CHART
  // =========================================================

  private createTicketChart(
    data: RepairDashboard
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

      type: 'doughnut',

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

            borderWidth: 0,

            hoverOffset: 8

          }

        ]

      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        cutout: '70%',

        animation: {

          duration: 900,

          easing: 'easeOutQuart'

        },

        plugins: {

          legend: {

            position: 'bottom',

            labels: {

              usePointStyle: true,

              padding: 20

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
    data: RepairDashboard
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

      type: 'bar',

      data: {

        labels: [
          'Pending',
          'Accepted',
          'Rejected'
        ],

        datasets: [

          {
            label: 'Demands',

            data: [
              data.pendingDemands,
              data.acceptedDemands,
              data.rejectedDemands
            ],

            borderRadius: 8,

            borderSkipped: false,

            maxBarThickness: 55

          }

        ]

      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        animation: {

          duration: 900,

          easing: 'easeOutQuart'

        },

        scales: {

          y: {

            beginAtZero: true,

            ticks: {

              precision: 0

            }

          },

          x: {

            grid: {

              display: false

            }

          }

        },

        plugins: {

          legend: {

            display: false

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

    if (this.ticketChart) {

      this.ticketChart.destroy();

      this.ticketChart = undefined;

    }


    if (this.demandChart) {

      this.demandChart.destroy();

      this.demandChart = undefined;

    }

  }


  // =========================================================
  // STATISTICS
  // =========================================================

  get totalTickets(): number {

    return this.dashboard()?.totalTickets ?? 0;

  }


  get pendingTickets(): number {

    return this.dashboard()?.pendingTickets ?? 0;

  }


  get acceptedTickets(): number {

    return this.dashboard()?.acceptedTickets ?? 0;

  }


  get completedTickets(): number {

    return this.dashboard()?.completedTickets ?? 0;

  }


  get activeRepairs(): number {

    return this.dashboard()?.activeRepairs ?? 0;

  }


  get completedRepairs(): number {

    return this.dashboard()?.completedRepairs ?? 0;

  }


  get cancelledRepairs(): number {

    return this.dashboard()?.cancelledRepairs ?? 0;

  }


  get totalDemands(): number {

    return this.dashboard()?.totalDemands ?? 0;

  }


  get pendingDemands(): number {

    return this.dashboard()?.pendingDemands ?? 0;

  }


  get acceptedDemands(): number {

    return this.dashboard()?.acceptedDemands ?? 0;

  }


  get rejectedDemands(): number {

    return this.dashboard()?.rejectedDemands ?? 0;

  }

}
