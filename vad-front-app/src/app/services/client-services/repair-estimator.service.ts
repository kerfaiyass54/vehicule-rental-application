import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RepairEstimateTask {
  task: string;
  estimatedMinutes: number;
  confidence: number;
  sourceDescription: string;
}

export interface RepairEstimate {
  description: string;
  tasks: RepairEstimateTask[];
  totalEstimatedMinutes: number;
  dataset: string;
  datasetUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class RepairEstimatorService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8092/repair-estimates';

  estimate(
    description: string,
    ticketType: string | null,
    vehicleName: string | null
  ): Observable<RepairEstimate> {
    return this.http.post<RepairEstimate>(
      this.apiUrl,
      {
        description,
        ticketType,
        vehicleName
      }
    );
  }
}
