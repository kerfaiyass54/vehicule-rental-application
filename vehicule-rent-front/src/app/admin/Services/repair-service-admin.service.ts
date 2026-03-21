import { Injectable , ChangeDetectionStrategy} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Repair} from "../../models/repair";

@Injectable({
  providedIn: 'root'
})
export class RepairServiceAdminService {
  baseUrl ="http://localhost:8080/repair/";
  constructor(private http: HttpClient) {
  }

  addRepair(repair: Repair,location: String){
    return this.http.post<any>(this.baseUrl + "add?location=" + location, repair);
  }

  deleteRepair(id: any){
    return this.http.get<any>(this.baseUrl + "delete/"+id);
  }

  updateRepair(repair: Repair){
    return this.http.post<any>(this.baseUrl + "update",repair);
  }

  getRepair(name:String){
    return this.http.get<any>(this.baseUrl + name + "/get");
  }

  getLocation(name: String){
    return this.http.get<any>(this.baseUrl + "location/" + name);
  }

  getAllRepairs(){
    return this.http.get<any[]>("http://localhost:8080/admin/repairs");
  }

  updateLocation(repair: String, location:String){
    return this.http.get<any[]>(this.baseUrl + "changeloc/" + repair + "?locationName=" + location);
  }



}
