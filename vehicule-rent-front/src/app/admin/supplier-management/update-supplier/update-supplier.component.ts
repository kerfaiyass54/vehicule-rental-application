import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

import {ActivatedRoute, Router} from "@angular/router";
import {SupplierServiceAdminService} from "../../Services/supplier-service-admin.service";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-update-supplier',
    imports: [
    FormsModule,
    ReactiveFormsModule,
],
    templateUrl: './update-supplier.component.html',
    styleUrl: './update-supplier.component.css'
})
export class UpdateSupplierComponent implements OnInit{
  newSupplierForm: FormGroup;
  locations:any[]=[];
  id:any;
  supplier: any;
  idSupp:any;

  constructor(private fb: FormBuilder,
              private supplierService:SupplierServiceAdminService,
              private router: Router,private toastrService:ToastrService, private route:ActivatedRoute ) {
    this.newSupplierForm = this.fb.group({
      name: new FormControl("",[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
      nationality:new FormControl("",[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
      mail: new FormControl("",[Validators.required,Validators.email]),
      password: new FormControl("",[Validators.required,Validators.minLength(6)]),
    });


  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.supplierService.getSupplier(this.id).subscribe(
      (supp)=>{
        this.idSupp = supp.idSupp;
        this.newSupplierForm = this.fb.group({
          name: new FormControl(supp.suppName,[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
          nationality:new FormControl(supp.nationality,[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
          mail: new FormControl(supp.email,[Validators.required,Validators.email]),
          password: new FormControl(supp.pass,[Validators.required,Validators.minLength(6)]),
        });
      }
    )
  }

  get name(){
    return this.newSupplierForm.get('name');
  }

  get nationality(){
    return this.newSupplierForm.get('nationality');
  }

  get mail(){
    return this.newSupplierForm.get('mail');
  }

  get password(){
    return this.newSupplierForm.get('password');
  }



  updateSupplier() {
    this.supplier = {
      idSupp:this.idSupp,
      suppName: this.newSupplierForm.value.name,
      nationality:this.newSupplierForm.value.nationality,
      email: this.newSupplierForm.value.mail,
      pass: this.newSupplierForm.value.password,
      role: "SUPPLIER",
    }

    this.supplierService.updateSupplier(this.supplier).subscribe(
      ()=>{
        this.toastrService.success("Supplier updated","SUCCESS");
        this.router.navigate(['admin/infos/supplier',this.newSupplierForm.value.name]);
      }
    )
  }

  return() {
    this.router.navigate(['admin/infos/supplier',this.id]);
  }
}
