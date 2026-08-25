import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


import { Page } from './client-buying.service';
import {VehiculeResult} from '../../client-ui/models/vehicule-result.model';
import {Transmission} from '../../client-ui/enums/transmission';
import {VehiculeStatus} from '../../client-ui/enums/vehicule-status';

@Injectable({
  providedIn: 'root'
})
export class VehiculeSearchService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8100/api/v1/vehicles';


  searchVehicles(
    keyword?: string,
    transmission?: Transmission,
    status?: VehiculeStatus,
    minPrice?: number,
    maxPrice?: number,
    page = 0,
    size = 10
  ): Observable<Page<VehiculeResult>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (keyword) {
      params = params.set('keyword', keyword);
    }

    if (transmission) {
      params = params.set('transmission', transmission);
    }

    if (status) {
      params = params.set('status', status);
    }

    if (minPrice !== undefined) {
      params = params.set('minPrice', minPrice);
    }

    if (maxPrice !== undefined) {
      params = params.set('maxPrice', maxPrice);
    }

    return this.http.get<Page<VehiculeResult>>(
      `${this.apiUrl}/search`,
      { params }
    );
  }
}
