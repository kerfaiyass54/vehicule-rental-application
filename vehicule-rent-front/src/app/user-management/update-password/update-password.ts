import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {KeycloakService} from "../../shared/keycloak.service";
import {UserManage} from "../services/user-manage";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-update-password',
    imports: [
        MatButton,
        MatDialogActions,
        MatDialogClose,
        MatDialogContent,
        MatDialogTitle,
        MatFormField,
        MatInput,
        MatLabel,
        ReactiveFormsModule
    ],
    templateUrl: './update-password.html',
    styleUrl: './update-password.css'
})
export class UpdatePassword implements OnInit{

  id: any;
  isLoggedIn = false;
  changePass: FormGroup;
  email: string | undefined;
  roles: string[] = [];
  role: string = '';
  passStrength: number = 0;

  constructor(private dialogRef: MatDialogRef<UpdatePassword>,private fb: FormBuilder,
              private keycloakService: KeycloakService, private userManager: UserManage, private toastr:ToastrService) {

    this.changePass = this.fb.group({
      newPassword: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required])
    });
  }

  async ngOnInit() {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    this.email = (await this.keycloakService.loadUserProfile()).email;
    this.id = (await this.keycloakService.loadUserProfile()).id;
    this.roles = this.keycloakService.getRoles();
    if (this.roles.includes('admin')) this.role='admin';
    if (this.roles.includes('client')) this.role='client';
    if (this.roles.includes('supplier')) this.role='supplier';
    if (this.roles.includes('repairer')) this.role='repairer';
  }


  get newPassword(){
    return this.changePass.get('newPassword');
  }

  get confirmPassword(){
    return this.changePass.get('confirmPassword');
  }


  updatePassword(item: any){
    if(item.newPassword == item.confirmPassword){
      let pass = {
        email: this.email,
        newPassword: item.newPassword,
        role: this.role
      }
      this.userManager.updatePassword(this.id,pass).subscribe(
        ()=>{
          this.toastr.success("SUCCESS","Password changed");
          window.location.reload();
        }
      )
    }
    else{
      this.toastr.error("ERROR","The passwords are not the same!");
    }
  }


  evaluatePassword(pass:any){
    this.userManager.predict(pass.newPassword).subscribe(
      (response: any)=>{
        this.passStrength = response.strength;
      }
    )
  }

}
