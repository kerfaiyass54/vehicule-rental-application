import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { RepairProfile } from '../../repair-ui/models/repair-profile.model';
import { Location } from '../../repair-ui/models/location.model';
import { RepairDashboard } from '../../repair-ui/models/repair-dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class RepairDetails {

  // =========================================================
  // HTTP
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API URL
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/repairs';


  // =========================================================
  // GET REPAIR CENTER PROFILE
  // =========================================================

  getInfo(
    repairEmail: string
  ): Observable<RepairProfile> {

    return this.http.get<RepairProfile>(
      `${this.apiUrl}/${encodeURIComponent(repairEmail)}`
    );

  }


  // =========================================================
  // GET LOCATIONS
  // =========================================================

  getLocations(): Observable<Location[]> {

    return this.http.get<Location[]>(
      `${this.apiUrl}/locations`
    );

  }


  // =========================================================
  // UPDATE REPAIR CENTER LOCATION
  // =========================================================

  updateLocation(
    repairEmail: string,
    locationId: number
  ): Observable<RepairProfile> {

    return this.http.put<RepairProfile>(
      `${this.apiUrl}/${encodeURIComponent(repairEmail)}/location/${locationId}`,
      {}
    );

  }


  // =========================================================
  // GET REPAIR CENTER DASHBOARD
  // =========================================================

  getDashboard(
    repairEmail: string
  ): Observable<RepairDashboard> {

    return this.http.get<RepairDashboard>(
      `${this.apiUrl}/${encodeURIComponent(repairEmail)}/dashboard`
    );

  }

}
