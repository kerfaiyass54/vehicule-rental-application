import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {CreateSupplierAddress} from '../../supplier-ui/models/create-supplier-address.model';
import {SupplierAddress} from '../../supplier-ui/models/supplier-address.model';
import {PageResponse} from '../../supplier-ui/models/page-response.model';
import {SupplierAddressResponse} from '../../supplier-ui/models/supplier-address-response.model';



@Injectable({
  providedIn: 'root'
})
export class SupplierAddresses {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1';

  /**
   * Returns the number of addresses belonging to a supplier.
   */
  getSupplierAddressesCount(
    email: string
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/suppliers/${encodeURIComponent(email)}/addresses/count`
    );
  }

  /**
   * Creates a new address and assigns it to a supplier.
   */
  addAddress(
    address: CreateSupplierAddress
  ): Observable<SupplierAddress> {

    return this.http.post<SupplierAddress>(
      `${this.apiUrl}/supplier-addresses`,
      address
    );
  }

  /**
   * Returns a paginated list of addresses belonging to a supplier.
   */
  getSupplierAddresses(
    email: string,
    page = 0,
    size = 10
  ): Observable<any> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<any>(
      `${this.apiUrl}/suppliers/${encodeURIComponent(email)}/addresses`,
      { params }
    );
  }

  /**
   * Frees an address and removes its supplier association.
   */
  freeAddress(
    addressId: number
  ): Observable<void> {

    return this.http.patch<void>(
      `${this.apiUrl}/supplier-addresses/${addressId}/free`,
      {}
    );
  }
}
