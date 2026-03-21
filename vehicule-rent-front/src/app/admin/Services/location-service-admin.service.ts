import { Injectable , ChangeDetectionStrategy} from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class LocationServiceAdminService {
  baseUrl ="http://localhost:8080/";
  constructor(private http: HttpClient) {}

  getRepairs(name: String){
    return this.http.get<any[]>(this.baseUrl + "location/repairs/" + name);
  }

  getSuppliers(name: String){
    return this.http.get<any[]>(this.baseUrl + "location/suppliers/" + name);
  }

  getClients(name: String){
    return this.http.get<any[]>(this.baseUrl + "location/clients/" + name);
  }

  addLocation(location: Location){
    return this.http.post<any>(this.baseUrl + "location/add", location);
  }

  deleteLocation(name: String){
    return this.http.get<any>(this.baseUrl + "location/delete/" + name );
  }

  getAllLocations(){
    return this.http.get<any[]>(this.baseUrl + "admin/locations" );
  }

  getLocation(name:String){
    return this.http.get<any>(this.baseUrl + "location/" + name + "/get");
  }

  getLocationNamesByCountry(country: String){
    return this.http.get<any>(this.baseUrl + "location/" + country);
  }

  getLocationsNames(){
    return this.http.get<any>(this.baseUrl + "location/locNames");
  }
}
