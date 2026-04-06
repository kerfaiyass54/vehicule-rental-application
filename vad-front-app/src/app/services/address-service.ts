import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AddressService {

  private baseUrl = 'http://localhost:8100/adress';

  constructor(private http: HttpClient) {}

  // Add address
  addAddress(address: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/`, address);
  }

  // Get supplier addresses (paginated)
  getSupplierAddresses(page: number, size: number, email: string): Observable<any> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('email', email);

    return this.http.get(`${this.baseUrl}/`, { params });
  }

  // Delete address
  deleteAddress(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // Get total addresses by supplier email
  getTotalAddresses(email: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/total/${email}`);
  }

  // Get number of addresses per location
  getAddressesPerLocation(locationName: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/location/${locationName}`);
  }

  // Get locations list by supplier email
  getLocations(email: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/locations/${email}`);
  }

  // Get countries list by supplier email
  getCountries(email: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/countries/${email}`);
  }

}
