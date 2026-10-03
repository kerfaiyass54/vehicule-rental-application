import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface VehicleRecommendation {
  vehicleId: number;
  vehicleName: string;
  brand: string;
  price: number;
  maxSpeed: number;
  transmission: string;
  status: string;
  score: number;
  reason: string;
}

export interface SupplierRecommendation {
  supplierId: number;
  supplierName: string;
  road: string;
  addressNumber: number;
  vehicles: VehicleRecommendation[];
}

export interface RecommendationResponse {
  email: string;
  locationName: string;
  budget: number;
  suppliers: SupplierRecommendation[];
  generatedAt: string;
}

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/recommendations`;

  request(email: string): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${encodeURIComponent(email)}/request`, {});
  }

  latest(email: string): Observable<RecommendationResponse> {
    return this.http.get<RecommendationResponse>(
      `${this.apiUrl}/${encodeURIComponent(email)}`);
  }
}
