import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LocationAnalysisRequest {
  period_start?: string;
  period_end?: string;
}

export interface LocationAnalysisRecord {
  id: string;
  requested_by: string | null;
  period_start: string;
  period_end: string;
  analysis: Record<string, unknown>;
  created_at: string;
}

@Injectable({
  providedIn: 'root',
})
export class LocationEdaManagementService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8060/api/analyses';

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
