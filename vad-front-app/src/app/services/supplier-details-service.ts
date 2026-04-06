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

}
