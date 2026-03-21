import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';

import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ClientServiceAdminService} from "../../Services/client-service-admin.service";
import {LocationServiceAdminService} from "../../Services/location-service-admin.service";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-update-client',
    imports: [
    ReactiveFormsModule,
],
    templateUrl: './update-client.component.html',
    styleUrl: './update-client.component.css'
})
export class UpdateClientComponent implements OnInit{
  updateClientForm: FormGroup;
  locations:any[]=[];
  id:any;
  client: any;
  names: any[]=[];


  constructor( private toastService:ToastrService, private fb: FormBuilder,private clientService:ClientServiceAdminService,private locationService:LocationServiceAdminService,private router:Router,private route:ActivatedRoute) {
    this.updateClientForm = this.fb.group({
      name: new FormControl("",[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
      cin:new FormControl("",[Validators.required,Validators.pattern("[0-9 ]*"),Validators.minLength(8),Validators.maxLength(8)]),
      mail: new FormControl("",[Validators.required,Validators.email]), password: new FormControl("",[Validators.required,Validators.minLength(6)]), loc: new FormControl("")
    });
  }


  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.locationService.getAllLocations().subscribe(
      (locs)=>{
        this.locations = locs;
      }
    )
    this.clientService.getClient(this.id).subscribe(
      (res)=>{
        this.client = res;
        this.updateClientForm = this.fb.group({
          name: new FormControl(this.client.nameClient,[Validators.required,Validators.pattern("[a-zA-Z ]*")]),
          cin:new FormControl(this.client.cin,[Validators.required,Validators.pattern("[0-9 ]*"),Validators.minLength(8),Validators.maxLength(8)]),
          mail: new FormControl(this.client.email,[Validators.required,Validators.email]), password: new FormControl(this.client.pass,[Validators.required,Validators.minLength(6)]), loc: new FormControl(this.client.locationName)
        });
      }
    );

    this.clientService.getNames().subscribe((names)=>{
      this.names = names;
      this.names = this.names.filter(el => el !== this.client.nameClient);
    });

  }

  get name(){
    return this.updateClientForm.get('name');
  }

  get cin(){
    return this.updateClientForm.get('cin');
  }

  get mail(){
    return this.updateClientForm.get('mail');
  }

  get password(){
    return this.updateClientForm.get('password');
  }

  get loc(){
    return this.updateClientForm.get('loc');
  }

  updateClient(){
    let client : any = {
      idClient: this.client.idClient,
      nameClient: this.updateClientForm.value.name,
      cin: this.updateClientForm.value.cin,
      budget: 0,
      email: this.updateClientForm.value.mail,
      pass: this.updateClientForm.value.password,
      role: "CLIENT",
    }

    if(this.updateClientForm.value.cin == this.client.cin && !this.names.includes(client.nameClient)){
      this.clientService.updateClient(client).subscribe(
        ()=>{
          this.toastService.success("Client updated","CLIENT");
        }
      )
      this.clientService.changeLocation(this.id,this.updateClientForm.value.loc).subscribe(
        ()=>{
          this.toastService.success("Location updated","LOCATION");
        }
      );
      this.router.navigate(['admin/clients/details-client'
        ,client.nameClient]);
    }
    else{

    }




  }

  return(){
    this.router.navigate(['admin/clients/details-client',this.id]);
  }


}
