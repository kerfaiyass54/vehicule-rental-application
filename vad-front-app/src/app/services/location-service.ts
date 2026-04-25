import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})

export class LocationService {

  private baseUrl = 'http://localhost:8100/location';


  constructor(private http: HttpClient) {}


  getCitiesByCountry(country: string) {

    return this.http.get<string[]>(

      `${this.baseUrl}/cities/${country}`

    );

  }


  // ✅ list of location names

  getLocationsNames(): Observable<string[]> {

    return this.http.get<string[]>(

      `${this.baseUrl}/names`

    );

  }


  // ✅ list of countries

  getCountries(): Observable<string[]> {

    return this.http.get<string[]>(

      `${this.baseUrl}/countries`

    );

  }

}
