import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ClientLocationService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/clients';


  getLocation(clientEmail: string): Observable<any> {

    return this.http.get<any>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/location`
    );
  }


  updateLocation(
    clientEmail: string,
    location: any
  ): Observable<any> {

    return this.http.put<any>(
      `${this.apiUrl}/${encodeURIComponent(clientEmail)}/location`,
      location
    );
  }
}
