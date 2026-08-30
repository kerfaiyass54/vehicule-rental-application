import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


import { TicketClient } from '../../repair-ui/models/ticket-client.model';
import { TicketVehicule } from '../../repair-ui/models/ticket-vehicule.model';
import { TicketDetailsModel } from '../../repair-ui/models/ticket-details.model';
import {Page} from '../client-services/client-buying.service';
import {RepairTicket} from '../../repair-ui/models/repair-ticket.model';
import {TicketDetails} from '../../repair-ui/repair-tickets/ticket-details/ticket-details';


@Injectable({
  providedIn: 'root'
})
export class RepairTicketService {

  // =========================================================
  // HTTP
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API URL
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/repair-tickets';


  // =========================================================
  // GET REPAIR TICKETS
  // =========================================================

  getTickets(
    repairEmail: string,
    page = 1,
    size = 10
  ): Observable<Page<RepairTicket>> {

    return this.http.get<Page<RepairTicket>>(
      this.apiUrl,
      {
        params: {
          repairEmail,
          page,
          size
        }
      }
    );

  }


  // =========================================================
  // GET TICKET DETAILS
  // =========================================================

  getTicketInfo(
    ticketId: number
  ): Observable<TicketDetailsModel> {

    return this.http.get<TicketDetailsModel>(
      `${this.apiUrl}/${ticketId}`
    );

  }


  // =========================================================
  // GET TICKET CLIENT
  // =========================================================

  getClient(
    ticketId: number
  ): Observable<TicketClient> {

    return this.http.get<TicketClient>(
      `${this.apiUrl}/${ticketId}/client`
    );

  }


  // =========================================================
  // GET TICKET VEHICULE
  // =========================================================

  getVehicule(
    ticketId: number
  ): Observable<TicketVehicule> {

    return this.http.get<TicketVehicule>(
      `${this.apiUrl}/${ticketId}/vehicule`
    );

  }

}
