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

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import { SupplierLocations as SupplierLocationService } from '../../services/supplier-services/supplier-location';

import { Location } from '../models/location.model';

@Component({
  selector: 'app-supplier-location',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './supplier-location.html',
  styleUrl: './supplier-location.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SupplierLocation implements OnInit, AfterViewInit, OnDestroy {

  private readonly keycloak = inject(Keycloak);

  private readonly locationService =
    inject(SupplierLocationService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // VIEW CHILDREN
  // =========================================================

  @ViewChildren('revealElement')
  private readonly revealElements!: QueryList<ElementRef>;


  private observer?: IntersectionObserver;


  // =========================================================
  // STATE
  // =========================================================

  readonly locations =
    signal<Location[]>([]);

  readonly countries =
    signal<string[]>([]);

  readonly locationNames =
    signal<string[]>([]);

  readonly supplierEmail =
    signal('');

  readonly loading =
    signal(true);

  readonly error =
    signal(false);


  // =========================================================
  // PAGINATION
  // =========================================================

  readonly page =
    signal(0);

  readonly size =
    signal(6);

  /*
   * The backend currently returns List<LocationDTO>
   * instead of Spring Page<LocationDTO>.
   *
   * Therefore we calculate pagination on the frontend.
   */
  readonly totalElements =
    signal(0);

  readonly totalPages =
    signal(0);


  // =========================================================
  // STATISTICS
  // =========================================================

  readonly uniqueCountries =
    signal(0);

  readonly visibleLocations =
    signal(0);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadSupplierEmail();
  }


  ngAfterViewInit(): void {
    this.setupScrollAnimations();
  }


  ngOnDestroy(): void {

    this.observer?.disconnect();

    this.destroy$.next();
    this.destroy$.complete();
  }


  // =========================================================
  // AUTHENTICATION
  // =========================================================

  private loadSupplierEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] ?? '';

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

    this.loadLocations();
  }


  // =========================================================
  // LOAD LOCATIONS
  // =========================================================

  loadLocations(): void {

    const email =
      this.supplierEmail();

    if (!email) {
      return;
    }

    this.loading.set(true);
    this.error.set(false);

    this.cdr.markForCheck();


    /*
     * The backend endpoint returns:
     *
     * List<LocationDTO>
     *
     * We therefore load the list and paginate it
     * on the frontend.
     */

    this.locationService
      .getLocations(
        email,
        this.page(),
        this.size()
      )
      .pipe(
        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

          /*
           * Give Angular time to render the cards
           * before reconnecting scroll animations.
           */
          setTimeout(() => {
            this.setupScrollAnimations();
          });
        })
      )
      .subscribe({

        next: locations => {

          const data =
            locations ?? [];

          this.locations.set(data);

          this.totalElements.set(
            data.length
          );

          this.visibleLocations.set(
            data.length
          );

          this.calculateStatistics(data);

          this.error.set(false);

          this.cdr.markForCheck();
        },

        error: error => {

          console.error(
            'Unable to load supplier locations',
            error
          );

          this.locations.set([]);

          this.totalElements.set(0);

          this.visibleLocations.set(0);

          this.uniqueCountries.set(0);

          this.error.set(true);

          this.cdr.markForCheck();
        }
      });
  }


  // =========================================================
  // STATISTICS
  // =========================================================

  private calculateStatistics(
    locations: Location[]
  ): void {

    const countrySet =
      new Set(
        locations
          .map(location => location.country)
          .filter(Boolean)
      );

    this.uniqueCountries.set(
      countrySet.size
    );
  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {
      return;
    }

    this.loadLocations();
  }


  // =========================================================
  // PAGINATION
  // =========================================================

  onPageChange(
    event: PageEvent
  ): void {

    this.page.set(
      event.pageIndex
    );

    this.size.set(
      event.pageSize
    );

    this.loadLocations();
  }


  // =========================================================
  // LOCATION HELPERS
  // =========================================================

  getLocationIcon(
    index: number
  ): string {

    const icons = [
      'location_city',
      'business',
      'apartment',
      'place',
      'explore',
      'public'
    ];

    return icons[
    index % icons.length
      ];
  }


  getCountryInitial(
    country: string
  ): string {

    if (!country) {
      return '?';
    }

    return country
      .trim()
      .charAt(0)
      .toUpperCase();
  }


  getLocationNumber(
    index: number
  ): string {

    return String(
      index + 1
    ).padStart(2, '0');
  }


  trackLocation(
    _index: number,
    location: Location
  ): number {

    return location.idLoc;
  }


  // =========================================================
  // COUNTRY DISTRIBUTION
  // =========================================================

  getCountryCount(
    country: string
  ): number {

    return this.locations()
      .filter(
        location =>
          location.country === country
      )
      .length;
  }


  getCountryPercentage(
    country: string
  ): number {

    const total =
      this.locations().length;

    if (!total) {
      return 0;
    }

    return Math.round(
      (
        this.getCountryCount(country) /
        total
      ) * 100
    );
  }


  getCountriesSorted(): string[] {

    return [
      ...new Set(
        this.locations()
          .map(location => location.country)
          .filter(Boolean)
      )
    ].sort(
      (a, b) =>
        this.getCountryCount(b) -
        this.getCountryCount(a)
    );
  }


  // =========================================================
  // SCROLL ANIMATIONS
  // =========================================================

  private setupScrollAnimations(): void {

    if (typeof IntersectionObserver === 'undefined') {
      return;
    }

    this.observer?.disconnect();

    this.observer =
      new IntersectionObserver(

        entries => {

          entries.forEach(entry => {

            if (entry.isIntersecting) {

              entry.target.classList.add(
                'visible'
              );

            }

          });

        },

        {
          threshold: 0.12
        }
      );


    /*
     * Query after Angular has rendered
     * the dynamic cards.
     */

    setTimeout(() => {

      this.revealElements
        ?.forEach(element => {

          this.observer?.observe(
            element.nativeElement
          );

        });

    });
  }
}
