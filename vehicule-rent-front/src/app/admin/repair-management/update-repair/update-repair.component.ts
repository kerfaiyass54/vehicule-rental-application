import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';

import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RepairServiceAdminService} from "../../Services/repair-service-admin.service";
import {LocationServiceAdminService} from "../../Services/location-service-admin.service";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-update-repair',
    imports: [
    ReactiveFormsModule,
],
    templateUrl: './update-repair.component.html',
    styleUrl: './update-repair.component.css'
})
export class UpdateRepairComponent implements OnInit {
  updateRepairForm:FormGroup;
  locations: any[] = [];


  constructor(
    private fb: FormBuilder,
              private repairService:RepairServiceAdminService,
              private locationService: LocationServiceAdminService, private router: Router,private toastrService:ToastrService ) {


    this.updateRepairForm = this.fb.group({
      name: new FormControl("",[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
      mail: new FormControl("",[Validators.required,Validators.email]),
      password: new FormControl("",[Validators.required,Validators.minLength(6)]),
      loc: new FormControl("")
    });


  }

  ngOnInit() {
    this.locationService.getAllLocations().subscribe(
      (res)=>{this.locations = res;
      }
    )


  }

  get name(){
    return this.updateRepairForm.get('name');
  }

  get mail(){
    return this.updateRepairForm.get('mail');
  }

  get password(){
    return this.updateRepairForm.get('password');
  }

  get loc(){
    return this.updateRepairForm.get('loc');
  }

  updateRepair() {

  }

  return() {

  }
}
