import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Buying} from '../../client-ui/models/buying.model';


export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ClientBuyingService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/buyings';


  addBuying(
    vehiculeId: number,
    clientEmail: string,
    period: number,
    renew: boolean
  ): Observable<Buying> {

    return this.http.post<Buying>(
      this.apiUrl,
      null,
      {
        params: {
          vehiculeId,
          clientEmail,
          period,
          renew
        }
      }
    );
  }


  getBuyings(
    clientEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<Buying>> {

    return this.http.get<Page<Buying>>(
      `${this.apiUrl}/clients/${encodeURIComponent(clientEmail)}`,
      {
        params: {
          page,
          size
        }
      }
    );
  }
}
