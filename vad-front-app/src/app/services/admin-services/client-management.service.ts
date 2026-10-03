import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ClientAdmin} from '../../admin-ui/models/client-admin.model';
import {Page} from '../../models/page.model';
import {Client} from '../../client-ui/models/client.model';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ClientManagementService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/api/v1/clients`;


  // =========================================================
  // GET CLIENT EMAILS
  // =========================================================

  getClientEmails(): Observable<string[]> {

    return this.http.get<string[]>(
      `${this.apiUrl}/emails`
    );

  }

  createClient(client: any): Observable<any> {

    return this.http.post<any>(
      this.apiUrl,
      client
    );

  }


  // =========================================================
  // GET CLIENTS
  // =========================================================

  getClients(
    page = 0,
    size = 10
  ): Observable<Page<ClientAdmin>> {

    return this.http.get<Page<ClientAdmin>>(
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
  // GET CLIENT
  // =========================================================

  getClient(
    id: number
  ): Observable<ClientAdmin> {

    return this.http.get<ClientAdmin>(
      `${this.apiUrl}/${id}`
    );

  }


  // =========================================================
  // UPDATE CLIENT
  // =========================================================

  updateClient(
    id: number,
    client: ClientAdmin
  ): Observable<ClientAdmin> {

    return this.http.put<ClientAdmin>(
      `${this.apiUrl}/${id}`,
      client
    );

  }


  // =========================================================
  // DELETE CLIENT
  // =========================================================

  deleteClient(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );

  }

}
