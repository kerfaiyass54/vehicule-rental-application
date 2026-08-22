import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DemandResponse } from '../../supplier-ui/models/demand-response.model';

@Injectable({
  providedIn: 'root'
})
export class SupplierDemand {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/suppliers';


  // ---------------------------------------------------------
  // GET PAGINATED DEMANDS
  // ---------------------------------------------------------

  getDemands(
    supplierEmail: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<DemandResponse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<DemandResponse>>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/demands`,
      { params }
    );
  }


  // ---------------------------------------------------------
  // APPROVE DEMAND
  // ---------------------------------------------------------

  approveDemand(
    demandId: number
  ): Observable<DemandResponse> {

    return this.http.patch<DemandResponse>(
      `${this.apiUrl}/demands/${demandId}/approve`,
      {}
    );
  }


  // ---------------------------------------------------------
  // REFUSE DEMAND
  // ---------------------------------------------------------

  refuseDemand(
    demandId: number
  ): Observable<DemandResponse> {

    return this.http.patch<DemandResponse>(
      `${this.apiUrl}/demands/${demandId}/refuse`,
      {}
    );
  }
}


// ---------------------------------------------------------
// SPRING PAGE RESPONSE
// ---------------------------------------------------------

export interface PageResponse<T> {

  content: T[];

  totalElements: number;

  totalPages: number;

  size: number;

  number: number;

  first: boolean;

  last: boolean;

  numberOfElements: number;

  empty: boolean;
}
