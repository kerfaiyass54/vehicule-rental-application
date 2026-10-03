import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Session} from '../models/Session';
import {PageResponse} from '../models/PageResponse';

@Injectable({
  providedIn: 'root',
})
export class SessionService {

  private readonly base = `http://localhost:8101/account`;

  constructor(private http: HttpClient) {}

  saveSession() {
    return this.http.post(`${this.base}/sessions`, {});
  }


  findByEmailPaged(email: string, page = 0, size = 5): Observable<PageResponse<Session>> {
    const params = new HttpParams()
      .set('email', email)
      .set('page', page)
      .set('size', size);
    return this.http.get<PageResponse<Session>>(`${this.base}/sessions`, { params });
  }

  findById(id: string): Observable<Session> {
    return this.http.get<Session>(`${this.base}/${id}`);
  }

  logoutAll(): Observable<void> {
    return this.http.post<void>(`${this.base}/me/logout-all`, {});
  }

}
