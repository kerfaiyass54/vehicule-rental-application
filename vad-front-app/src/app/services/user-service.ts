import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private readonly base = `${environment.accountApiUrl}/account`;

  constructor(private http: HttpClient) {}

  updateUser(userId: string, dto: any): Observable<void> {
    return this.http.put<void>(`${this.base}/me`, {
      username: dto.username ?? `${dto.firstName}.${dto.lastName}`,
      firstName: dto.firstName,
      lastName: dto.lastName,
      email: dto.newEmail ?? dto.email
    });
  }

  updatePassword(userId: string, dto: any): Observable<void> {
    return this.http.put<void>(`${this.base}/me/password`, {
      newPassword: dto.newPassword
    });
  }

  logoutAll(): Observable<void> {
    return this.http.post<void>(`${this.base}/me/logout-all`, {});
  }

  // DELETE /keycloak/?id=...&role=...&email=...
  deleteUser(id: string, role: string, email: string): Observable<void> {
    const params = new HttpParams()
      .set('id', id)
      .set('role', role)
      .set('email', email);
    return this.http.delete<void>(`${this.base}/`, { params });
  }

}
