import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SubscriptionInfo} from '../../client-ui/models/subscription-info.model';
import {SupplierInfo} from '../../client-ui/models/supplier-info.model';
import {SubscriptionType} from '../../client-ui/enums/subscription-type';
import {Page} from './client-buying.service';



@Injectable({
  providedIn: 'root'
})
export class ClientSubscriptionService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/clients';


  addSubscription(
    subscription: SubscriptionInfo
  ): Observable<SubscriptionInfo> {

    return this.http.post<SubscriptionInfo>(
      `${this.apiUrl}/subscriptions`,
      subscription
    );
  }

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


  renewSubscription(
    clientEmail: string
  ): Observable<SubscriptionInfo> {

    return this.http.put<SubscriptionInfo>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/subscription/renew`,
      null
    );
  }


  cancelSubscription(
    clientEmail: string
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/subscription`
    );
  }


  getSubscribedSuppliers(
    clientId: number
  ): Observable<SupplierInfo[]> {

    return this.http.get<SupplierInfo[]>(
      `${this.apiUrl}/subscribed`,
      {
        params: {
          clientId
        }
      }
    );
  }


  getUnsubscribedSuppliers(
    clientId: number
  ): Observable<SupplierInfo[]> {

    return this.http.get<SupplierInfo[]>(
      `${this.apiUrl}/unsubscribed`,
      {
        params: {
          clientId
        }
      }
    );
  }


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
