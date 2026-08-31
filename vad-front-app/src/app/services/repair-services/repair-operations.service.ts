import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from '../client-services/client-buying.service';

import { RepairInfoModel } from '../../repair-ui/models/repair-info.model';


@Injectable({
  providedIn: 'root'
})
export class RepairOperationsService {

  // =========================================================
  // HTTP
  // =========================================================

  private readonly http =
    inject(HttpClient);


  // =========================================================
  // API URL
  // =========================================================

  private readonly apiUrl =
    'http://localhost:8100/api/v1/repair-operations';


  // =========================================================
  // GET REPAIR OPERATIONS
  // =========================================================

  getRepairInfos(
    repairEmail: string,
    page = 0,
    size = 10
  ): Observable<Page<RepairInfoModel>> {

    return this.http.get<Page<RepairInfoModel>>(
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
  // GET REPAIR INFORMATION BY ID
  // =========================================================

  getRepairInfo(
    repairInfoId: number
  ): Observable<RepairInfoModel> {

    return this.http.get<RepairInfoModel>(
      `${this.apiUrl}/${repairInfoId}`
    );

  }


  // =========================================================
  // START REPAIR
  // =========================================================

  startRepair(
    repairInfoId: number
  ): Observable<RepairInfoModel> {

    return this.http.post<RepairInfoModel>(
      `${this.apiUrl}/start/${repairInfoId}`,
      {}
    );

  }


  // =========================================================
  // CANCEL REPAIR
  // =========================================================

  cancelRepair(
    repairInfoId: number
  ): Observable<void> {

    return this.http.patch<void>(
      `${this.apiUrl}/${repairInfoId}/cancel`,
      {}
    );

  }

}
