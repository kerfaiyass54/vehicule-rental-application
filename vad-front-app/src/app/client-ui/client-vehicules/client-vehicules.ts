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

import { Subject, finalize, takeUntil } from 'rxjs';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTableModule } from '@angular/material/table';

import { ClientVehiculeService } from '../../services/client-services/client-vehicule.service';
import { OwnedVehicule } from '../models/owned-vehicule.model';
import { Transmission } from '../enums/transmission';

@Component({
  selector: 'app-client-vehicules',
  standalone: true,

  imports: [
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],

  templateUrl: './client-vehicules.html',
  styleUrl: './client-vehicules.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientVehicules implements OnInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly clientVehiculeService =
    inject(ClientVehiculeService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly vehicules =
    signal<OwnedVehicule[]>([]);

  readonly filteredVehicules =
    signal<OwnedVehicule[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly searchTerm =
    signal('');

  readonly clientEmail =
    signal('');

  readonly page =
    signal(0);

  readonly size =
    signal(5);

  readonly totalElements =
    signal(0);

  readonly displayedColumns = [
    'vehicle',
    'brand',
    'transmission',
    'supplier'
  ];


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadClientEmail();
    this.setupScrollReveal();
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadClientEmail(): void {

    const token = this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.error.set(true);
      this.loading.set(false);

      this.cdr.markForCheck();

      return;
    }

    this.clientEmail.set(email);

    this.loadVehicules();
  }


  // =========================================================
  // LOAD VEHICLES
  // =========================================================

  loadVehicules(): void {

    const email = this.clientEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();

    this.clientVehiculeService
      .getOwnedVehicules(
        email,
        this.page(),
        this.size()
      )
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();
        })
      )
      .subscribe({

        next: response => {

          const vehicles =
            response.content ?? [];

          this.vehicules.set(vehicles);

          this.filteredVehicules.set(
            this.filterVehicles(
              vehicles,
              this.searchTerm()
            )
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.error.set(false);

          this.cdr.markForCheck();

          setTimeout(() => {
            this.animateRows();
          }, 50);
        },

        error: error => {

          console.error(
            'Unable to load client vehicles',
            error
          );

          this.vehicules.set([]);
          this.filteredVehicules.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    const value =
      input.value.trim().toLowerCase();

    this.searchTerm.set(value);

    const filtered =
      this.filterVehicles(
        this.vehicules(),
        value
      );

    this.filteredVehicules.set(filtered);

    this.cdr.markForCheck();

    setTimeout(() => {
      this.animateRows();
    }, 50);
  }


  private filterVehicles(
    vehicles: OwnedVehicule[],
    search: string
  ): OwnedVehicule[] {

    if (!search) {
      return vehicles;
    }

    return vehicles.filter(vehicle => {

      return (
        vehicle.nameVehicule
          ?.toLowerCase()
          .includes(search) ||

        vehicle.brand
          ?.toLowerCase()
          .includes(search) ||

        vehicle.supplierName
          ?.toLowerCase()
          .includes(search) ||

        vehicle.transmission
          ?.toString()
          .toLowerCase()
          .includes(search)
      );
    });
  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadVehicules();
  }


  // =========================================================
  // PAGINATION
  // =========================================================

  onPageChange(event: PageEvent): void {

    this.page.set(event.pageIndex);

    this.size.set(event.pageSize);

    this.loadVehicules();
  }


  // =========================================================
  // HELPERS
  // =========================================================

  getTransmissionLabel(
    transmission: Transmission
  ): string {

    return transmission === Transmission.AUTOMATIC
      ? 'Automatic'
      : 'Manual';
  }


  getTransmissionIcon(
    transmission: Transmission
  ): string {

    return transmission === Transmission.AUTOMATIC
      ? 'auto_mode'
      : 'settings';
  }


  getInitials(
    name: string
  ): string {

    if (!name) {
      return '?';
    }

    return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(part => part.charAt(0))
      .join('')
      .toUpperCase();
  }


  trackVehicle(
    _index: number,
    vehicle: OwnedVehicule
  ): string {

    return `${vehicle.nameVehicule}-${vehicle.brand}-${vehicle.supplierName}`;
  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private setupScrollReveal(): void {

    if (typeof IntersectionObserver === 'undefined') {
      return;
    }

    setTimeout(() => {

      const elements =
        document.querySelectorAll(
          '.reveal'
        );

      const observer =
        new IntersectionObserver(
          entries => {

            entries.forEach(entry => {

              if (entry.isIntersecting) {

                entry.target.classList.add(
                  'visible'
                );

              } else {

                entry.target.classList.remove(
                  'visible'
                );

              }

            });
          },
          {
            threshold: 0.12
          }
        );

      elements.forEach(element => {
        observer.observe(element);
      });

    }, 100);
  }


  // =========================================================
  // ROW ANIMATION
  // =========================================================

  private animateRows(): void {

    const rows =
      document.querySelectorAll(
        '.vehicle-row'
      );

    rows.forEach((row, index) => {

      const element =
        row as HTMLElement;

      element.style.animationDelay =
        `${index * 70}ms`;

      element.classList.remove(
        'row-visible'
      );

      void element.offsetWidth;

      element.classList.add(
        'row-visible'
      );
    });
  }
}
