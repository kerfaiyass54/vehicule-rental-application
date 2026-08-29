import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SubscriptionInfo } from '../../client-ui/models/subscription-info.model';
import { SupplierInfo } from '../../client-ui/models/supplier-info.model';
import { SubscriptionType } from '../../client-ui/enums/subscription-type';
import { Page } from './client-buying.service';

@Injectable({
  providedIn: 'root'
})
export class ClientSubscriptionService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/clients';


  // =========================================================
  // CREATE SUBSCRIPTION
  // =========================================================

  addSubscription(
    subscription: SubscriptionInfo
  ): Observable<SubscriptionInfo> {

    return this.http.post<SubscriptionInfo>(
      `${this.apiUrl}/subscriptions`,
      subscription
    );
  }


  // =========================================================
  // GET CLIENT SUBSCRIPTIONS
  // =========================================================

  getSubscriptions(
    clientEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<SubscriptionInfo>> {

    return this.http.get<Page<SubscriptionInfo>>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/subscription/list`,
      {
        params: {
          page,
          size
        }
      }
    );
  }


  // =========================================================
  // RENEW SUBSCRIPTION
  // =========================================================

  renewSubscription(
    clientEmail: string
  ): Observable<SubscriptionInfo> {

    return this.http.put<SubscriptionInfo>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/subscription/renew`,
      null
    );
  }


  // =========================================================
  // CANCEL SUBSCRIPTION
  // =========================================================

  cancelSubscription(
    clientEmail: string
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/subscription`
    );
  }


  // =========================================================
  // GET SUBSCRIBED SUPPLIERS
  // =========================================================

  getSubscribedSuppliers(
    clientEmail: string
  ): Observable<SupplierInfo[]> {

    return this.http.get<SupplierInfo[]>(
      `${this.apiUrl}/subscribed`,
      {
        params: {
          clientEmail
        }
      }
    );
  }


  // =========================================================
  // GET UNSUBSCRIBED SUPPLIERS
  // =========================================================

  getUnsubscribedSuppliers(
    clientEmail: string
  ): Observable<SupplierInfo[]> {

    return this.http.get<SupplierInfo[]>(
      `${this.apiUrl}/unsubscribed`,
      {
        params: {
          clientEmail
        }
      }
    );
  }

  // =========================================================
// GET SUBSCRIPTION DETAILS
// =========================================================

  getSubscriptionDetails(
    clientEmail: string,
    supplierEmail: string
  ): Observable<SubscriptionInfo> {

    return this.http.get<SubscriptionInfo>(
      `${this.apiUrl}/subscription/details`,
      {
        params: {
          clientEmail,
          supplierEmail
        }
      }
    );
  }


  // =========================================================
  // GET REDUCTION
  // =========================================================

  getReduction(
    subscriptionType: SubscriptionType
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/reduction`,
      {
        params: {
          subscriptionType
        }
      }
    );
  }
}
