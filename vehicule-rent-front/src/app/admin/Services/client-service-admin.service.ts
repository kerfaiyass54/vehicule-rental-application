import { Injectable , ChangeDetectionStrategy} from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import {Client} from "../../models/client";
import {PaginatedResponse} from "../../models/paginatedResponse";


@Injectable({
  providedIn: 'root'
})
export class ClientServiceAdminService {
  baseUrlAdmin ="http://localhost:8100/admin/";
  baseUrlClient = "http://localhost:8100/client/"
    constructor(private http:HttpClient) {}

  createClient(client: Client, location:String){
    return  this.http.post<any>(this.baseUrlClient + "new?locationName=" + location,client);
  }

  getAllClients(id: any){
      return this.http.get<any[]>(this.baseUrlAdmin + "clients/" + id);
  }

  deleteClient(id: any){
      return this.http.delete<any>(this.baseUrlClient + "/" + id);
  }

  getClient(name: any){
      return this.http.get<any>(this.baseUrlClient + "/" + name);
  }

  updateClient(client: Client){
      return this.http.put<any>(this.baseUrlClient, client);
  }

  changeLocation(client: String, location: String){
    return this.http.get<any>(this.baseUrlClient + "location/" + client + "?newLocation=" + location );
  }

  getClients(page: number = 0, size: number = 5, search?: string){
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    if (search) params = params.set('search', search);
    return this.http.get<PaginatedResponse<Client>>(this.baseUrlClient + "list/clients", { params });
  }

  getNames(){
    return this.http.get<any>(this.baseUrlAdmin + "names");
  }

  getClientEmails(){
    return this.http.get<any>(this.baseUrlClient + "emails");
  }




}
