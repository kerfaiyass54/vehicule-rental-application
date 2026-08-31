import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Admin} from '../../admin-ui/models/admin.model';
import { AdminDashboardModel} from '../../admin-ui/models/admin-dashboard.model';



@Injectable({
  providedIn: 'root'
})
export class AdminDetailsService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8100/api/v1/admins';


  // =========================================================
  // GET ADMIN DETAILS
  // =========================================================

  getDetails(
    email: any
  ): Observable<Admin> {

    return this.http.get<Admin>(
      `${this.apiUrl}/${email}`
    );

  }


  // =========================================================
  // UPDATE ADMIN DETAILS
  // =========================================================

  updateDetails(
    email: any,
    admin: Admin
  ): Observable<void> {

    return this.http.put<void>(
      `${this.apiUrl}/${email}`,
      admin
    );

  }


  // =========================================================
  // GET DASHBOARD
  // =========================================================

  getDashboard(): Observable<AdminDashboardModel> {

    return this.http.get<AdminDashboardModel>(
      `${this.apiUrl}/dashboard`
    );

  }

}
