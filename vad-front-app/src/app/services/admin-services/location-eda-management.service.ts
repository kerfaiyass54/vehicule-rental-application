import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LocationAnalysisRequest } from '../../models/location-analysis-request.model';
import { LocationAnalysisRecord } from '../../models/location-analysis-record.model';

@Injectable({
  providedIn: 'root',
})
export class LocationEdaManagementService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.locationEdaApiUrl}/api/analyses`;

  runAnalysis(
    email: string,
    request: LocationAnalysisRequest = {},
  ): Observable<LocationAnalysisRecord> {
    const params = new HttpParams().set('email', email);
    return this.http.post<LocationAnalysisRecord>(this.apiUrl, request, { params });
  }

  getAnalyses(
    email: string,
    limit = 20,
  ): Observable<LocationAnalysisRecord[]> {
    const params = new HttpParams()
      .set('email', email)
      .set('limit', limit);
    return this.http.get<LocationAnalysisRecord[]>(this.apiUrl, { params });
  }
}
