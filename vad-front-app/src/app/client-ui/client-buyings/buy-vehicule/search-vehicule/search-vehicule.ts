import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import {ClientVehiculeService} from '../../../../services/client-services/client-vehicule.service';
import {VehiculeSearchDTO} from '../../../models/vehicule-search.model';



@Component({
  selector: 'app-search-vehicule',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './search-vehicule.html',
  styleUrl: './search-vehicule.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SearchVehicule implements OnChanges {

  private readonly vehiculeService =
    inject(ClientVehiculeService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // INPUT
  // =========================================================

  @Input()
  supplierId!: number;


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  vehicleSelected =
    new EventEmitter<VehiculeSearchDTO>();


  // =========================================================
  // STATE
  // =========================================================

  vehicles: VehiculeSearchDTO[] = [];

  selectedVehicle: VehiculeSearchDTO | null = null;

  loading = false;

  error = false;

  errorMessage = '';


  // =========================================================
  // PAGINATION
  // =========================================================

  page = 0;

  size = 8;

  totalElements = 0;

  totalPages = 0;


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    if (
      changes['supplierId'] &&
      this.supplierId
    ) {

      this.page = 0;

      this.selectedVehicle = null;

      this.loadVehicles();

    }

  }


  // =========================================================
  // LOAD VEHICLES
  // =========================================================

  loadVehicles(): void {

    if (!this.supplierId) {
      return;
    }

    this.loading = true;

    this.error = false;

    this.errorMessage = '';

    this.vehicles = [];

    this.vehiculeService
      .getSupplierVehicules(
        this.supplierId,
        this.page,
        this.size
      )
      .pipe(
        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();

        })
      )
      .subscribe({

        next: response => {

          this.vehicles =
            response.content ?? [];

          this.totalElements =
            response.totalElements ?? 0;

          this.totalPages =
            response.totalPages ?? 0;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load supplier vehicles',
            error
          );

          this.error = true;

          this.errorMessage =
            'Unable to load vehicles for this supplier.';

          this.vehicles = [];

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SELECT VEHICLE
  // =========================================================

  selectVehicle(
    vehicle: VehiculeSearchDTO
  ): void {

    this.selectedVehicle = vehicle;

    this.vehicleSelected.emit(vehicle);

    this.cdr.markForCheck();

  }


  // =========================================================
  // PAGINATION
  // =========================================================

  nextPage(): void {

    if (
      this.page >= this.totalPages - 1 ||
      this.loading
    ) {
      return;
    }

    this.page++;

    this.selectedVehicle = null;

    this.loadVehicles();

  }


  previousPage(): void {

    if (
      this.page <= 0 ||
      this.loading
    ) {
      return;
    }

    this.page--;

    this.selectedVehicle = null;

    this.loadVehicles();

  }


  // =========================================================
  // HELPERS
  // =========================================================

  isSelected(
    vehicle: VehiculeSearchDTO
  ): boolean {

    return (
      this.selectedVehicle?.idVehicule ===
      vehicle.idVehicule
    );

  }


  getStatusClass(
    status: string
  ): string {

    return status
      ?.toLowerCase()
      .replace(/\s+/g, '-') ?? '';

  }

}
