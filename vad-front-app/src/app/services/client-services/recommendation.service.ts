import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RecommendationResponse } from '../../models/recommendation-response.model';

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
