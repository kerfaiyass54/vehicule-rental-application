import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from './client-buying.service';

import { TicketInfo } from '../../client-ui/models/ticket-info.model';
import { OpenTicket } from '../../client-ui/models/open-ticket.model';
import {Repair} from '../../client-ui/models/repair.model';

@Injectable({
  providedIn: 'root'
})
export class ClientTicketService {

  // =========================================================
  // HTTP
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API URL
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/tickets';


  // =========================================================
  // GET CLIENT TICKETS
  // =========================================================

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

  getRepairs(
    page = 0,
    size = 10
  ): Observable<Page<Repair>> {

    return this.http.get<Page<Repair>>(
      `${this.apiUrl}/repairs`,
      {
        params: {
          page,
          size
        }
      }
    );

  }


  // =========================================================
  // OPEN TICKET
  // =========================================================

  openTicket(
    ticket: OpenTicket
  ): Observable<TicketInfo> {

    return this.http.post<TicketInfo>(
      this.apiUrl,
      ticket
    );

  }

}
