import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SupplierDetails} from '../../supplier-ui/models/supplier-details.model';
import {SupplierDashboard} from '../../supplier-ui/models/supplier-dashboard.model';



@Injectable({
  providedIn: 'root'
})
export class SupplierDetailsService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/suppliers';

  getSupplierDetails(
    supplierEmail: string
  ): Observable<SupplierDetails> {

    return this.http.get<SupplierDetails>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/details`
    );
  }

  getSupplierDashboard(
    supplierEmail: string
  ): Observable<SupplierDashboard> {

    return this.http.get<SupplierDashboard>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/dashboard`
    );
  }
}
