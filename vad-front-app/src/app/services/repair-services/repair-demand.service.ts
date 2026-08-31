import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Page} from '../client-services/client-buying.service';
import {DemandsListPage} from '../../repair-ui/models/DemandsListPage';
import {DemandDetails} from '../../repair-ui/models/demand-details.model';
import {CreateDemand} from '../../repair-ui/models/create-demand.model';
import {RepairTicket} from '../../repair-ui/models/repair-ticket.model';


@Injectable({
  providedIn: 'root'
})
export class RepairDemandService {

  // =========================================================
  // HTTP
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API URL
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/repair-demands';


  // =========================================================
  // GET REPAIR DEMANDS
  // =========================================================

  getDemands(
    repairEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<DemandsListPage>> {

    return this.http.get<Page<DemandsListPage>>(
      `${this.apiUrl}/repairs/${encodeURIComponent(repairEmail)}`,
      {
        params: {
          page,
          size
        }
      }
    );

  }

  // =========================================================
// GET SUPPLIER EMAIL BY VEHICULE
// =========================================================

  getSupplierEmail(
    vehiculeId: number
  ): Observable<string> {

    return this.http.get<string>(
      `${this.apiUrl}/vehicules/${vehiculeId}/supplier/email`
    );

  }


  getDemandDetails(
    demandId: number
  ): Observable<DemandDetails> {

    return this.http.get<DemandDetails>(
      `${this.apiUrl}/${demandId}`
    );

  }

  createDemand(
    demand: CreateDemand
  ): Observable<RepairTicket> {

    return this.http.post<RepairTicket>(
      this.apiUrl,
      demand
    );

  }


}
