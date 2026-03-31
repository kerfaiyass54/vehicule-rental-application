import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private readonly base = `http://localhost:8100/keycloak`;

  constructor(private http: HttpClient) {}

  // PUT /keycloak/?userID=...
  // body: { email, firstName, lastName, role, newEmail }  → UpdateUserDTO
  updateUser(userId: string, dto: any): Observable<void> {
    const params = new HttpParams().set('userID', userId);
    return this.http.put<void>(`${this.base}/`, dto, { params });
  }

  // PUT /keycloak/password?id=...
  // body: { email, newPassword, role }  → PasswordDTO
  updatePassword(userId: string, dto: any): Observable<void> {
    const params = new HttpParams().set('id', userId);
    return this.http.put<void>(`${this.base}/password`, dto, { params });
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
