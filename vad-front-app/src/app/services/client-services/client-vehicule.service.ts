import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from './client-buying.service';
import { OwnedVehicule } from '../../client-ui/models/owned-vehicule.model';
import { VehiculeSearchDTO } from '../../client-ui/models/vehicule-search.model';

@Injectable({
  providedIn: 'root'
})
export class ClientVehiculeService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/clients';


  // =========================================================
  // GET OWNED VEHICLES
  // =========================================================

  getOwnedVehicules(
    clientEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<OwnedVehicule>> {

    return this.http.get<Page<OwnedVehicule>>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/vehicles`,
      {
        params: {
          page,
          size
        }
      }
    );
  }


  // =========================================================
  // GET SUPPLIER VEHICLES
  // =========================================================

  getSupplierVehicules(
    supplierId: number,
    page = 0,
    size = 10
  ): Observable<Page<VehiculeSearchDTO>> {

    return this.http.get<Page<VehiculeSearchDTO>>(
      `${this.apiUrl}/${supplierId}/vehicules`,
      {
        params: {
          page,
          size
        }
      }
    );
  }


  // =========================================================
  // GET TOTAL PRICE
  // =========================================================

  getTotalPrice(
    vehiculeId: number,
    reduction: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/total-price`,
      {
        params: {
          vehiculeId,
          reduction
        }
      }
    );
  }

}
