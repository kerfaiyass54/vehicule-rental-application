import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserManage {

  baseUrlKeycloak = "http://localhost:8100/keycloak/";
  passwordCheck = "http://127.0.0.1:8000/predict";

  constructor(private http: HttpClient) {}

  createUser(user: any){
    return this.http.post<any>(this.baseUrlKeycloak, user);
  }

  predict(password: any) {
    return this.http.post(this.passwordCheck, { password });
  }

  getUsers(){
    return this.http.get<any[]>(this.baseUrlKeycloak + "users");
  }

  deleteUser(id: any, role: any, email: any){
    const params = new HttpParams()
      .set('id', id)
      .set('role', role)
      .set('email', email);

    return this.http.delete<any>(this.baseUrlKeycloak, { params });
  }

  updatePassword(id: any, password: any){
    const params = new HttpParams().set('id', id);
    return this.http.put<any>(this.baseUrlKeycloak + "password", password, { params });
  }

  getRoles(){
    return this.http.get<any[]>(this.baseUrlKeycloak + "roles");
  }

  updateUser(userId: any, updateUser: any){
    const params = new HttpParams().set('userID', userId);
    return this.http.put<any>(this.baseUrlKeycloak, updateUser, { params });
  }

  assignRole(userId: any, role: any){
    const params = new HttpParams()
      .set('userId', userId)
      .set('roleName', role);

    return this.http.patch<any>(this.baseUrlKeycloak + "role", null, { params });
  }

}
