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

import { FormsModule } from '@angular/forms';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  LocationAdmin
} from '../models/location-admin.model';

import {
  LocationManagementService
} from '../../services/admin-services/location-management.service';


@Component({
  selector: 'app-admin-locations',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,

    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './admin-locations.html',

  styleUrl: './admin-locations.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminLocations
  implements OnInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly locationService =
    inject(LocationManagementService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // DATA
  // =========================================================

  readonly locations =
    signal<LocationAdmin[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly page =
    signal(0);

  readonly size =
    signal(6);

  readonly totalElements =
    signal(0);


  // =========================================================
  // SEARCH
  // =========================================================

  searchTerm = '';


  // =========================================================
  // DIALOG
  // =========================================================

  showDialog = false;

  editing = false;

  saving = false;

  dialogError = '';


  // =========================================================
  // FORM
  // =========================================================

  locationForm: LocationAdmin = {

    id: 0,

    name: '',

    country: '',

    position: ''

  };


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadLocations();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD
  // =========================================================

  loadLocations(): void {

    this.loading.set(true);

    this.error.set(false);

    this.cdr.markForCheck();


    this.locationService

      .getLocations(
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

          this.locations.set(
            response.content ?? []
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load locations:',
            error
          );

          this.locations.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // FILTER
  // =========================================================

  get filteredLocations(): LocationAdmin[] {

    const search =
      this.searchTerm
        .trim()
        .toLowerCase();


    if (!search) {

      return this.locations();

    }


    return this.locations().filter(
      location =>

        location.name
          ?.toLowerCase()
          .includes(search)

        ||

        location.country
          ?.toLowerCase()
          .includes(search)

        ||

        location.position
          ?.toLowerCase()
          .includes(search)

    );

  }


  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    this.searchTerm =
      input.value;

    this.cdr.markForCheck();

  }


  clearSearch(): void {

    this.searchTerm = '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // CREATE
  // =========================================================

  openCreateDialog(): void {

    this.editing = false;

    this.dialogError = '';

    this.locationForm = {

      id: 0,

      name: '',

      country: '',

      position: ''

    };

    this.showDialog = true;

    this.cdr.markForCheck();

  }


  // =========================================================
  // EDIT
  // =========================================================

  openEditDialog(
    location: LocationAdmin
  ): void {

    this.editing = true;

    this.dialogError = '';

    this.locationForm = {

      id: location.id,

      name: location.name,

      country: location.country,

      position: location.position

    };

    this.showDialog = true;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE DIALOG
  // =========================================================

  closeDialog(): void {

    if (this.saving) {

      return;

    }

    this.showDialog = false;

    this.dialogError = '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // SAVE
  // =========================================================

  saveLocation(): void {

    if (this.saving) {

      return;

    }


    const name =
      this.locationForm.name.trim();

    const country =
      this.locationForm.country.trim();

    const position =
      this.locationForm.position.trim();


    if (!name) {

      this.dialogError =
        'Location name is required.';

      return;

    }


    if (!country) {

      this.dialogError =
        'Country is required.';

      return;

    }


    if (!position) {

      this.dialogError =
        'Position is required.';

      return;

    }


    this.saving = true;

    this.dialogError = '';

    this.cdr.markForCheck();


    const dto: LocationAdmin = {

      id: this.locationForm.id,

      name,

      country,

      position

    };


    const request$ = this.editing

      ? this.locationService.updateLocation(
        dto.id,
        dto
      )

      : this.locationService.createLocation(
        dto
      );


    request$

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.saving = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.showDialog = false;

          this.loadLocations();

        },

        error: error => {

          console.error(
            'Unable to save location:',
            error
          );

          this.dialogError =
            'Unable to save location. Please try again.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // DELETE
  // =========================================================

  deleteLocation(
    location: LocationAdmin
  ): void {

    const confirmed =
      window.confirm(
        `Are you sure you want to delete "${location.name}"?`
      );


    if (!confirmed) {

      return;

    }


    this.locationService

      .deleteLocation(
        location.id
      )

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: () => {

          this.loadLocations();

        },

        error: error => {

          console.error(
            'Unable to delete location:',
            error
          );

        }

      });

  }


  // =========================================================
  // VIEW
  // =========================================================

  viewLocation(
    location: LocationAdmin
  ): void {

    window.alert(
      `${location.name}\n\nCountry: ${location.country}\nPosition: ${location.position}`
    );

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

    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================================
  // HELPERS
  // =========================================================

  getInitials(
    name: string
  ): string {

    if (!name) {

      return 'L';

    }


    return name

      .trim()

      .split(/\s+/)

      .slice(0, 2)

      .map(
        part =>
          part
            .charAt(0)
            .toUpperCase()
      )

      .join('');

  }


  trackLocation(
    _index: number,
    location: LocationAdmin
  ): number {

    return location.id;

  }

}
