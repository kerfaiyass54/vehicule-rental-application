import { Injectable } from '@angular/core';

import {
  HttpClient
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';
import { environment } from '../../environments/environment';




@Injectable({
  providedIn: 'root',
})
export class RecommandService {

  private apiUrl =
    `${environment.apiUrl}/api/recommendations`;

  constructor(
    private http: HttpClient
  ) {}

  getRecommendations(
    vehicleId: number
  ): Observable<any> {

    return this.http.get<any>(

      `${this.apiUrl}/${vehicleId}`

    );

  }

}
