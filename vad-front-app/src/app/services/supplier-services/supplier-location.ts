import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Location } from '../../supplier-ui/models/location.model';

@Injectable({
  providedIn: 'root'
})
export class SupplierLocations {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/suppliers';


  // ---------------------------------------------------------
  // GET LOCATION NAMES
  // ---------------------------------------------------------

  getLocationNames(
    supplierEmail: string
  ): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/locations/names`
    );
  }


  // ---------------------------------------------------------
  // GET COUNTRIES
  // ---------------------------------------------------------

  getCountries(
    supplierEmail: string
  ): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/locations/countries`
    );
  }


  // ---------------------------------------------------------
  // GET SUPPLIER LOCATIONS
  // ---------------------------------------------------------

  getLocations(
    supplierEmail: string,
    page: number = 0,
    size: number = 10
  ): Observable<Location[]> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Location[]>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/locations`,
      { params }
    );
  }
}
