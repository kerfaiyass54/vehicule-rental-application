import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {LocationAdmin} from '../../admin-ui/models/location-admin.model';
import {Page} from '../../admin-ui/models/page.model';



@Injectable({
  providedIn: 'root'
})
export class LocationManagementService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/locations';


  // =========================================================
  // GET LOCATION NAMES
  // =========================================================

  getLocationNames(): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/names`
    );

  }


  // =========================================================
  // GET COUNTRIES
  // =========================================================

  getCountries(): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/countries`
    );

  }


  // =========================================================
  // GET CITIES BY COUNTRY
  // =========================================================

  getCitiesByCountry(
    country: string
  ): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/cities`,
      {
        params: {
          country
        }
      }
    );

  }


  // =========================================================
  // CREATE LOCATION
  // =========================================================

  createLocation(
    location: LocationAdmin
  ): Observable<LocationAdmin> {

    return this.http.post<LocationAdmin>(
      this.apiUrl,
      location
    );

  }


  // =========================================================
  // GET LOCATIONS
  // =========================================================

  getLocations(
    page = 0,
    size = 10
  ): Observable<Page<LocationAdmin>> {

    return this.http.get<Page<LocationAdmin>>(
      this.apiUrl,
      {
        params: {
          page,
          size
        }
      }
    );

  }


  // =========================================================
  // GET LOCATION
  // =========================================================

  getLocation(
    id: number
  ): Observable<LocationAdmin> {

    return this.http.get<LocationAdmin>(
      `${this.apiUrl}/${id}`
    );

  }


  // =========================================================
  // UPDATE LOCATION
  // =========================================================

  updateLocation(
    id: number,
    location: LocationAdmin
  ): Observable<LocationAdmin> {

    return this.http.put<LocationAdmin>(
      `${this.apiUrl}/${id}`,
      location
    );

  }


  // =========================================================
  // DELETE LOCATION
  // =========================================================

  deleteLocation(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );

  }

}
