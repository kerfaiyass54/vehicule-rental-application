import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {SupplierServiceAdminService} from "../Services/supplier-service-admin.service";
import {Router} from "@angular/router";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-supplier-management',
    imports: [
],
    templateUrl: './supplier-management.component.html',
    styleUrl: './supplier-management.component.css'
})
export class SupplierManagementComponent implements OnInit{
  listSuppliers: any[] = [];

  constructor(private supplierService: SupplierServiceAdminService, private router: Router) {
  }

  ngOnInit() {
    this.supplierService.getAllSupp().subscribe(
      (data)=>{
        this.listSuppliers = data;
      }
    )
  }


  goToAddSupplier() {
    this.router.navigate(['admin/add-supplier']);
  }

  goToInfos(supp:any) {
    this.router.navigate(['admin/infos/supplier',supp]);
  }
}
