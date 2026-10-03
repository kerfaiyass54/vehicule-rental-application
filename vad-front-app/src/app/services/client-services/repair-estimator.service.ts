import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RepairEstimate } from '../../models/repair-estimate.model';

@Injectable({
  providedIn: 'root'
})
export class RepairEstimatorService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = `${environment.repairEstimatorApiUrl}/repair-estimates`;

  estimate(
    description: string,
    ticketType: string | null,
    vehicleName: string | null
  ): Observable<RepairEstimate> {
    return this.http.post<RepairEstimate>(
      this.apiUrl,
      {
        description,
        ticketType,
        vehicleName
      }
    );
  }
}
