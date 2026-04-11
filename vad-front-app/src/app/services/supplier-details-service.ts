import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SupplierDetailsDTO {
  suppName: string;
  nationality: string;
  email: string;
  experience: number;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class SupplierDetailsService {

  private baseUrl = 'http://localhost:8100/supplier';

  constructor(private http: HttpClient) {}

  getSupplierDetails(email: string): Observable<SupplierDetailsDTO> {
    return this.http.get<SupplierDetailsDTO>(
      `${this.baseUrl}/details/${email}`
    );
  }

  getSupplierVehicules(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/vehicules/${email}`
    );
  }

  getSupplierCategories(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/categories/${email}`
    );
  }

  getSupplierAddresses(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/addresses/${email}`
    );
  }

  // ✅ number of countries
  getSupplierCountries(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/countries/${email}`
    );
  }

// ✅ number of locations
  getSupplierLocations(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/locations/${email}`
    );
  }

// ✅ paginated addresses list
  getAddressesList(email: string, size: number, page: number): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.baseUrl}/addresses/list/${email}?size=${size}&page=${page}`
    );
  }

// ✅ countries list
  getCountriesList(email: string): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.baseUrl}/countries/list/${email}`
    );
  }

// ✅ paginated locations DTO list
  getLocationsList(email: string, size: number, page: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/locations/list/${email}?size=${size}&page=${page}`
    );
  }

}
