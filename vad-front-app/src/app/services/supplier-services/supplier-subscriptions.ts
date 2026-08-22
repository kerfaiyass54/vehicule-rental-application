import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SubscriptionResponse } from '../../supplier-ui/models/subscription-response.model';

@Injectable({
  providedIn: 'root'
})
export class SupplierSubscriptionsService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/suppliers';


  // ---------------------------------------------------------
  // PAGINATED SUBSCRIPTIONS
  // ---------------------------------------------------------

  getSubscriptions(
    supplierEmail: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<SubscriptionResponse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<SubscriptionResponse>>(
      `${this.apiUrl}/${encodeURIComponent(supplierEmail)}/subscriptions`,
      { params }
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
