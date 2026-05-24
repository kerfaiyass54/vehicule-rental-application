import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({
  providedIn: 'root',
})
export class RecommandService {

  private apiUrl = 'http://localhost:8100/api/recommendations';

  constructor(private http: HttpClient) {}

  getSupplierRecommendations(
    supplierEmail: string
  ): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.apiUrl}/supplier/${supplierEmail}`
    );
  }
}
