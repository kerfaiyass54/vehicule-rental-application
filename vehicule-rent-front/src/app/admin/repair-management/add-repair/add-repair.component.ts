import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

import {Router, RouterLink} from "@angular/router";
import {ClientServiceAdminService} from "../../Services/client-service-admin.service";
import {LocationServiceAdminService} from "../../Services/location-service-admin.service";
import {RepairServiceAdminService} from "../../Services/repair-service-admin.service";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-add-repair',
    imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink
],
    templateUrl: './add-repair.component.html',
    styleUrl: './add-repair.component.css'
})
export class AddRepairComponent implements OnInit{
  newRepairForm: FormGroup;
  locations: any[] = [];


  constructor(private fb: FormBuilder, private repairService:RepairServiceAdminService, private locationService: LocationServiceAdminService, private router: Router,private toastrService:ToastrService ) {


    this.newRepairForm = this.fb.group({
      name: new FormControl("",[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
      mail: new FormControl("",[Validators.required,Validators.email]),
      password: new FormControl("",[Validators.required,Validators.minLength(6)]),
      loc: new FormControl("")
    });


  }

  ngOnInit() {

    this.locationService.getAllLocations().subscribe(
      (res)=>{
        this.locations = res;
        console.log(this.locations);
      }
    )
  }

  get name(){
    return this.newRepairForm.get('name');
  }

  get mail(){
    return this.newRepairForm.get('mail');
  }

  get password(){
    return this.newRepairForm.get('password');
  }

  get loc(){
    return this.newRepairForm.get('loc');
  }

  addRepair() {
    let repair:any ={
      nameRepair: this.newRepairForm.value.name,
      email: this.newRepairForm.value.mail,
      pass: this.newRepairForm.value.password,
      role: "REPAIR",
    }

    this.repairService.addRepair(repair, this.newRepairForm.value.loc).subscribe(
      ()=>{
        this.toastrService.success("Repair added","SUCCESS");
        this.router.navigate(['admin/repair']);
      }
    )
  }
}
