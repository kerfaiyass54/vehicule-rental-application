import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SupplierDetailsDTO {
  suppName: any;
  nationality: any;
  email: any;
  experience: number;
  role: any;
}

@Injectable({
  providedIn: 'root',
})
export class SupplierDetailsService {

  private baseUrl = 'http://localhost:8100/supplier';

  constructor(private http: HttpClient) {}

  getSupplierDetails(email: any): Observable<SupplierDetailsDTO> {
    return this.http.get<SupplierDetailsDTO>(
      `${this.baseUrl}/details/${email}`
    );
  }

  getSupplierVehicules(email: any): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/vehicules/${email}`
    );
  }

  getSupplierCategories(email: any): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/categories/${email}`
    );
  }

  getSupplierAddresses(email: any): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/addresses/${email}`
    );
  }

  // ✅ number of countries
  getSupplierCountries(email: any): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/countries/${email}`
    );
  }

// ✅ number of locations
  getSupplierLocations(email: any): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/locations/${email}`
    );
  }

// ✅ paginated addresses list
  getAddressesList(email: any, size: number, page: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/addresses/list/${email}?size=${size}&page=${page}`
    );
  }

// ✅ countries list
  getCountriesList(email: any): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/countries/list/${email}`
    );
  }

// ✅ paginated locations DTO list
  getLocationsList(email: any, size: number, page: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/locations/list/${email}?size=${size}&page=${page}`
    );
  }



}
