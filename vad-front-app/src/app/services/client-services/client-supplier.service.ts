import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


import { Page } from './client-buying.service';
import {SupplierInfo} from '../../client-ui/models/supplier-info.model';
import {VehiculeSupplier} from '../../client-ui/models/vehicule-supplier.model';

@Injectable({
  providedIn: 'root'
})
export class ClientSupplierService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/suppliers';


  getSuppliers(
    page = 0,
    size = 10
  ): Observable<Page<SupplierInfo>> {

    return this.http.get<Page<SupplierInfo>>(
      this.apiUrl,
      {
        params: {
          page,
          size
        }
      }
    );
  }


  searchSuppliers(
    keyword = '',
    page = 0,
    size = 10
  ): Observable<Page<SupplierInfo>> {

    return this.http.get<Page<SupplierInfo>>(
      `${this.apiUrl}/search`,
      {
        params: {
          keyword,
          page,
          size
        }
      }
    );
  }


  getAvailableVehicules(
    supplierId: number,
    page = 0,
    size = 10
  ): Observable<Page<VehiculeSupplier>> {

    return this.http.get<Page<VehiculeSupplier>>(
      `${this.apiUrl}/supplier/${supplierId}/available`,
      {
        params: {
          page,
          size
        }
      }
    );
  }
}
