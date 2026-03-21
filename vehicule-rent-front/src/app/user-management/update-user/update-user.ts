import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatButtonModule} from '@angular/material/button';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ReactiveFormsModule, FormGroup, FormControl, Validators, FormBuilder} from "@angular/forms";
import { MatDialogRef } from '@angular/material/dialog';
import {KeycloakService} from "../../shared/keycloak.service";
import {UserManage} from "../services/user-manage";
import {getKeycloak} from "../../shared/keycloak-init";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-update-user',
    imports: [
        MatDialogContent,
        MatDialogActions,
        MatDialogClose,
        MatDialogTitle,
        MatButtonModule,
        MatFormField,
        MatInput,
        MatLabel,
        ReactiveFormsModule
    ],
    templateUrl: './update-user.html',
    styleUrl: './update-user.css'
})
export class UpdateUser implements OnInit{

  userForm: FormGroup;
  userInfo?: any;
  emails:string[] = [];
  users:any[] = [];
  isLoggedIn = false;
  role = '';
  roles:string[] = [];
  emailUser:any;
  id: any;


  constructor(private dialogRef: MatDialogRef<UpdateUser>,private fb: FormBuilder,
              private keycloakService: KeycloakService, private userManager: UserManage, private toastr:ToastrService) {

    this.userForm = this.fb.group({
      firstName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      lastName: new FormControl('', [Validators.required])
    });


  }

  async ngOnInit() {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    if (this.isLoggedIn) {
      this.userInfo = await this.keycloakService.loadUserProfile();
      this.emailUser = this.userInfo.email;
      console.log(this.userInfo);
      this.id = this.userInfo.id;
      this.roles = this.keycloakService.getRoles();
      this.userForm = this.fb.group({
        firstName: new FormControl(this.userInfo.firstName, [Validators.required]),
        email: new FormControl(this.userInfo.email, [Validators.required, Validators.email]),
        lastName: new FormControl(this.userInfo.lastName, [Validators.required])
      });
      if (this.roles.includes('admin')) this.role='admin';
      if (this.roles.includes('client')) this.role='client';
      if (this.roles.includes('supplier')) this.role='supplier';
      if (this.roles.includes('repairer')) this.role='repairer';
    }
    console.log(getKeycloak().tokenParsed);
        this.userManager.getUsers().subscribe(
          (users) =>{
            this.users = users;
            this.emails = this.users.map(user => user.email);
            this.emails.splice(this.emails.indexOf(this.userInfo.email),1);
            console.log(this.emails);
          }
        );

  }


  get firstName(){
    return this.userForm.get('firstName');
  }


  get lastName(){
    return this.userForm.get('lastName');
  }

  get email(){
    return this.userForm.get('email');
  }





  update(){
    if(!this.emails.includes(this.userForm.value.email)){
      let user = {
        firstName: this.userForm.value.firstName,
        lastName: this.userForm.value.lastName,
        email: this.emailUser,
        newEmail: this.userForm.value.email,
        role: this.role
      }
      this.userManager.updateUser(this.id,user).subscribe(
        ()=>{
          this.toastr.success("SUCCESS","User updated!");
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        }
      );
    }
    else{
      this.toastr.error("ERROR","The email already exist!");
    }





  }



}
