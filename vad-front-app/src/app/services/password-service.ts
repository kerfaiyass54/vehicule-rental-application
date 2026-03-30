import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PasswordService {

  private readonly base = `http://localhost:8000`;

  constructor(private http: HttpClient) {}

  predict(password: string): Observable<any> {
    return this.http.post<any>(`${this.base}/predict`, { password });
  }

}
