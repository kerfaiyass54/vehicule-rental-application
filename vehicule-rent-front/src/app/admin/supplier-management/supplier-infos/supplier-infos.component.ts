import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";

import {SupplierServiceAdminService} from "../../Services/supplier-service-admin.service";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-supplier-infos',
    imports: [
    RouterLink
],
    templateUrl: './supplier-infos.component.html',
    styleUrl: './supplier-infos.component.css'
})
export class SupplierInfosComponent implements OnInit{
  supplier:any;
  adresses: any[]=[];
  id:any;
  isThereAdress = false;
  constructor(private supplierService:SupplierServiceAdminService, private router:Router, private route:ActivatedRoute ) {

  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.supplierService.getSupplier(this.id).subscribe(
      (res)=>{
        this.supplier = res;
      }
    )
  }


  confirmDelete() {
    this.supplierService.deleteSupplier(this.supplier.suppName).subscribe(
      ()=>{
        console.log("done");
        this.router.navigate(['admin/supplier']);
      }
    )
  }

  goToUpdate() {
    this.router.navigate(['admin/update/supplier',this.id]);
  }
}
