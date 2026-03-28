import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Session} from '../models/Session';
import {PageResponse} from '../models/PageResponse';

@Injectable({
  providedIn: 'root',
})
export class SessionService {

  private readonly base = `localhost:8100/sessions`;

  constructor(private http: HttpClient) {}

  save(request: unknown): Observable<void> {
    return this.http.post<void>(`${this.base}/`, request);
  }

  findAll(): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.base}/`);
  }

  findByDate(date: string, id: string): Observable<Session[]> {
    const params = new HttpParams()
      .set('date', date)
      .set('id', id);
    return this.http.get<Session[]>(`${this.base}/by-date`, { params });
  }

  findByEmail(email: string): Observable<Session[]> {
    const params = new HttpParams()
      .set('email', email);
    return this.http.get<Session[]>(`${this.base}/by-email`, { params });
  }

  findByEmailPaged(email: string, page = 0, size = 5): Observable<PageResponse<Session>> {
    const params = new HttpParams()
      .set('email', email)
      .set('page', page)
      .set('size', size);
    return this.http.get<PageResponse<Session>>(`${this.base}/list/sessions`, { params });
  }

  findById(id: string): Observable<Session> {
    return this.http.get<Session>(`${this.base}/${id}`);
  }

}
