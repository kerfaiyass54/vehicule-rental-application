import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PasswordService {

  private readonly base = `${environment.passwordApiUrl}`;

  constructor(private http: HttpClient) {}

  predict(password: string): Observable<any> {
    return this.http.post<any>(`${this.base}/predict`, { password });
  }

}
