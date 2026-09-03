import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Client} from '../../client-ui/models/client.model';
import {ClientDashboardModel} from '../../client-ui/models/client-dashboard.model';
import {ClientDashboard} from '../../client-ui/client-dashboard/client-dashboard';



@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/clients';


  getClient(clientEmail: string): Observable<Client> {
    return this.http.get<Client>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/details`
    );
  }


  getDashboard(clientEmail: string): Observable<ClientDashboardModel> {
    return this.http.get<ClientDashboardModel>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/dashboard`
    );
  }


  getBudget(clientEmail: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiUrl}/budget`,
      {
        params: {
          clientEmail
        }
      }
    );
  }


  reduceBudget(
    clientEmail: string,
    valueToRemove: number
  ): Observable<void> {

    return this.http.patch<void>(
      `${this.apiUrl}/budget/reduce`,
      null,
      {
        params: {
          clientEmail,
          valueToRemove
        }
      }
    );
  }
}
