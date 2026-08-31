import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SupplierAdmin} from '../../admin-ui/models/supplier-admin.model';
import {Page} from '../../admin-ui/models/page.model';


@Injectable({
  providedIn: 'root'
})
export class SupplierManagementService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/admin/suppliers';


  // =========================================================
  // GET SUPPLIERS
  // =========================================================

  getSuppliers(
    page = 0,
    size = 10
  ): Observable<Page<SupplierAdmin>> {

    return this.http.get<Page<SupplierAdmin>>(
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
  // GET SUPPLIER
  // =========================================================

  getSupplier(
    id: number
  ): Observable<SupplierAdmin> {

    return this.http.get<SupplierAdmin>(
      `${this.apiUrl}/${id}`
    );

  }


  // =========================================================
  // UPDATE SUPPLIER
  // =========================================================

  updateSupplier(
    id: number,
    supplier: SupplierAdmin
  ): Observable<SupplierAdmin> {

    return this.http.put<SupplierAdmin>(
      `${this.apiUrl}/${id}`,
      supplier
    );

  }


  // =========================================================
  // DELETE SUPPLIER
  // =========================================================

  deleteSupplier(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );

  }

}
