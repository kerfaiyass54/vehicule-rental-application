import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SentVehicle {
  vehicleName: string;
  brand: string;
  color: string;
  price: number;
  maxSpeed: number;
  transmission: string;
  status: string;
  fuelType: string;
  horsepower: number;
  matchScore: number;
  reason: string;
}

export interface VehicleSenderResponse {
  interpreted: Record<string, string | number | null>;
  vehicles: SentVehicle[];
  dataset: string;
}

@Injectable({ providedIn: 'root' })
export class VehicleSenderService {
  private readonly http = inject(HttpClient);
  private readonly url = 'http://localhost:8091/vehicle-sender';

  generate(text: string): Observable<VehicleSenderResponse> {
    return this.http.post<VehicleSenderResponse>(this.url, { text });
  }
}
