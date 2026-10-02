import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { BudgetEvent } from '../../client-ui/models/budget-event.model';

interface BudgetHistoryHit {
  _source?: BudgetEvent;
}

@Injectable({ providedIn: 'root' })
export class BudgetService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8062/budget/history';
  private readonly clientApiUrl = 'http://localhost:8100/api/v1/clients';

  saveCurrentBudget(email: string): Observable<void> {
    return this.http.post<void>(`${this.clientApiUrl}/budget/snapshot`, null, {
      params: { clientEmail: email },
    });
  }

  getHistory(email: string): Observable<BudgetEvent[]> {
    const params = new HttpParams()
      .set('email', email)
      .set('start_date', '2000-01-01T00:00:00Z')
      .set('end_date', '2100-01-01T00:00:00Z')
      .set('size', '100');

    return this.http.get<BudgetHistoryHit[]>(this.apiUrl, { params }).pipe(
      map(hits => hits
        .map(hit => hit._source)
        .filter((event): event is BudgetEvent => event !== undefined)
        .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime()))
    );
  }
}
