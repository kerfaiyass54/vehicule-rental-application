import {
  Injectable,
  inject
} from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';
import {LocationValidation} from '../models/location-validation.model';




@Injectable({
  providedIn: 'root'
})
export class UserLocationValidationService {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/user-location-validation';


  // =========================================================
  // GET ALL USER NAMES
  // =========================================================

  getAllNames(): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/names`
    );

  }


  // =========================================================
  // GET ALL EMAILS
  // =========================================================

  getAllEmails(): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/emails`
    );

  }


  // =========================================================
  // GET ALL LOCATIONS
  // =========================================================

  getAllLocations(): Observable<LocationValidation[]> {

    return this.http.get<LocationValidation[]>(
      `${this.apiUrl}/locations`
    );

  }


  // =========================================================
  // CHECK NAME
  // =========================================================

  nameExists(
    name: string
  ): Observable<boolean> {

    const params =
      new HttpParams()
        .set(
          'name',
          name
        );

    return this.http.get<boolean>(
      `${this.apiUrl}/names/exists`,
      { params }
    );

  }


  // =========================================================
  // CHECK EMAIL
  // =========================================================

  emailExists(
    email: string
  ): Observable<boolean> {

    const params =
      new HttpParams()
        .set(
          'email',
          email
        );

    return this.http.get<boolean>(
      `${this.apiUrl}/emails/exists`,
      { params }
    );

  }


  // =========================================================
  // CHECK LOCATION
  // =========================================================

  locationExists(
    name: string,
    country: string
  ): Observable<boolean> {

    const params =
      new HttpParams()
        .set(
          'name',
          name
        )
        .set(
          'country',
          country
        );

    return this.http.get<boolean>(
      `${this.apiUrl}/locations/exists`,
      { params }
    );

  }

}
