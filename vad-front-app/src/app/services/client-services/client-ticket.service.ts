import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from './client-buying.service';
import {TicketInfo} from '../../client-ui/models/ticket-info.model';

@Injectable({
  providedIn: 'root'
})
export class ClientTicketService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/tickets';


  getTickets(
    clientEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<TicketInfo>> {

    return this.http.get<Page<TicketInfo>>(
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
