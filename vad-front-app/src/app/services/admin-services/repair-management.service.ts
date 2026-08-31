import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { RepairAdmin } from '../../admin-ui/models/repair-admin.model';
import { RepairCreation } from '../../admin-ui/models/repair-creation.model';
import { Page } from '../../admin-ui/models/page.model';

@Injectable({
  providedIn: 'root'
})
export class RepairManagementService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/repairs-management';


  // =========================================================
  // GET REPAIR CENTERS
  // =========================================================

  getRepairs(
    page = 0,
    size = 10
  ): Observable<Page<RepairAdmin>> {

    return this.http.get<Page<RepairAdmin>>(
      this.apiUrl,
      {
        params: {
          page,
          size
        }
      }
    );

  }


  // =========================================================
  // GET REPAIR CENTER
  // =========================================================

  getRepair(
    id: number
  ): Observable<RepairAdmin> {

    return this.http.get<RepairAdmin>(
      `${this.apiUrl}/${id}`
    );

  }


  // =========================================================
  // CREATE REPAIR CENTER
  // =========================================================

  createRepair(
    repair: any
  ): Observable<RepairAdmin> {

    return this.http.post<RepairAdmin>(
      this.apiUrl,
      repair
    );

  }


  // =========================================================
  // UPDATE REPAIR CENTER
  // =========================================================

  updateRepair(
    id: number,
    repair: RepairAdmin
  ): Observable<RepairAdmin> {

    return this.http.put<RepairAdmin>(
      `${this.apiUrl}/${id}`,
      repair
    );

  }


  // =========================================================
  // DELETE REPAIR CENTER
  // =========================================================

  deleteRepair(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );

  }

}
