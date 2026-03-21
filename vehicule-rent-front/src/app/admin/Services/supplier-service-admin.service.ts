import { Injectable , ChangeDetectionStrategy} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Supplier} from "../../models/supplier";

@Injectable({
  providedIn: 'root'
})
export class SupplierServiceAdminService {
  baseUrl ="http://localhost:8080/supp/";

  constructor(private http: HttpClient) {}

  addSupplier(supplier: Supplier){
    return this.http.post<any>(this.baseUrl + "add",supplier);
  }

  updateSupplier(supplier: Supplier){
    return this.http.put<any>(this.baseUrl + "update",supplier);
  }

  deleteSupplier(supplier: String){
    return this.http.get<any>(this.baseUrl + "delete/"+ supplier);
  }

  getSupplier(name: String){
    return this.http.get<any>(this.baseUrl + "supplier/" + name);
  }

  getAllSupp(){
    return this.http.get<any[]>("http://localhost:8080/admin/suppliers");
  }

}
