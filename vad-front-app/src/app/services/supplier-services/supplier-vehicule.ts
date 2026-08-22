import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Vehicule } from '../../supplier-ui/models/vehicule.model';
import { VehiculeList } from '../../supplier-ui/models/vehicule-list.model';
import { CreateVehicule } from '../../supplier-ui/models/create-vehicule.model';
import { VehiculeStatus } from '../../supplier-ui/models/vehicule-status.enum';

@Injectable({
  providedIn: 'root'
})
export class SupplierVehicule {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/suppliers';


  // ---------------------------------------------------------
  // TOTAL VEHICLES
  // GET /suppliers/{supplierEmail}/vehicles/total
  // ---------------------------------------------------------

  getTotalVehicles(
    supplierEmail: string
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles/total`
    );
  }


  // ---------------------------------------------------------
  // COUNT VEHICLES BY STATUS
  // GET /suppliers/{supplierEmail}/vehicles/count-by-status
  // ---------------------------------------------------------

  countVehiclesByStatus(
    supplierEmail: string,
    status: VehiculeStatus
  ): Observable<number> {

    const params = new HttpParams()
      .set('status', status);

    return this.http.get<number>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles/count-by-status`,
      { params }
    );
  }


  // ---------------------------------------------------------
  // PAGINATED VEHICLES
  // GET /suppliers/vehicles
  // ---------------------------------------------------------

  getVehiclesPaged(
    supplierEmail: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<VehiculeList>> {

    const params = new HttpParams()
      .set('supplierEmail', supplierEmail)
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<VehiculeList>>(
      `${this.apiUrl}/vehicles`,
      { params }
    );
  }


  // ---------------------------------------------------------
  // ALL VEHICLES
  // GET /suppliers/{supplierEmail}/vehicles
  // ---------------------------------------------------------

  getVehicles(
    supplierEmail: string
  ): Observable<Vehicule[]> {

    return this.http.get<Vehicule[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles`
    );
  }


  // ---------------------------------------------------------
  // CREATE VEHICLE
  // POST /suppliers/{supplierEmail}/vehicles
  // ---------------------------------------------------------

  addVehicle(
    supplierEmail: string,
    vehicle: CreateVehicule
  ): Observable<Vehicule> {

    return this.http.post<Vehicule>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles`,
      vehicle
    );
  }


  // ---------------------------------------------------------
  // VEHICLE NAMES
  // GET /suppliers/{supplierEmail}/vehicles/names
  // ---------------------------------------------------------

  getVehicleNames(
    supplierEmail: string
  ): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles/names`
    );
  }


  // ---------------------------------------------------------
  // VEHICLE IDS
  // GET /suppliers/{supplierEmail}/vehicles/ids
  // ---------------------------------------------------------

  getVehicleIds(
    supplierEmail: string
  ): Observable<number[]> {

    return this.http.get<number[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/vehicles/ids`
    );
  }
}


// ---------------------------------------------------------
// SPRING PAGE RESPONSE
// ---------------------------------------------------------

export interface PageResponse<T> {

  content: T[];

  totalElements: number;

  totalPages: number;

  size: number;

  number: number;

  first: boolean;

  last: boolean;

  numberOfElements: number;

  empty: boolean;
}
