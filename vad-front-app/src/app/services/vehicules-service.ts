import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface VehiculeDTO {
  idVehicule: number;
  nameVehicule: string;
  color: string;
  brand: string;
  price: number;
  highSpeed: number;
  transmission: string;
  vehiculeStatus: string;
  supplier?: string;
}

export interface VehiculeUpdate {
  idVehicule: number;
  color: string;
  price: number;
  highSpeed: number;
}

@Injectable({
  providedIn: 'root',
})
export class VehiculesService {

  private baseUrl = 'http://localhost:8100/supplier';

  constructor(private http: HttpClient) {}

  /*
  ===============================
  VEHICULE STATISTICS
  ===============================
  */

  getTotalVehicules(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/vehicules/total/${email}`
    );
  }

  getVehiculesByStatus(
    email: string,
    status: 'AVAILABLE' | 'TAKEN' | 'REPARATION'
  ): Observable<number> {

    return this.http.get<number>(
      `${this.baseUrl}/vehicules/status/${email}?status=${status}`
    );
  }

  /*
  ===============================
  VEHICULE LIST
  ===============================
  */

  getVehiculesList(email: string): Observable<VehiculeDTO[]> {

    return this.http.get<VehiculeDTO[]>(
      `${this.baseUrl}/vehicules/list/${email}`
    );
  }

  /*
  ===============================
  UPDATE VEHICULE
  ===============================
  */

  updateVehicule(data: VehiculeUpdate): Observable<string> {

    return this.http.put<string>(
      `${this.baseUrl}/vehicules`,
      data
    );
  }

  /*
  ===============================
  ADD VEHICULE
  ===============================
  */

  addVehicule(data: VehiculeDTO): Observable<string> {

    return this.http.post<string>(
      `${this.baseUrl}/vehicules`,
      data
    );
  }

}
