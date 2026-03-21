import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {UserManage} from "../services/user-manage";
import {KeycloakService} from "../../shared/keycloak.service";
import { MatDialogRef } from '@angular/material/dialog';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-delete-account-dialog',
    imports: [
        MatDialogActions,
        MatDialogTitle,
        MatDialogContent,
        MatButton,
        MatDialogClose
    ],
    templateUrl: './delete-account-dialog.html',
    styleUrl: './delete-account-dialog.css'
})
export class DeleteAccountDialog implements OnInit{

  id: any;
  isLoggedIn = false;
  role: any;
  email: any;
  roles: string[] = [];

  constructor(private userManage: UserManage, private keycloakService: KeycloakService) {}

  async ngOnInit() {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    if(this.isLoggedIn){
      this.id = (await this.keycloakService.loadUserProfile()).id;
      this.roles = this.keycloakService.getRoles();
      if (this.roles.includes('admin')) this.role='You are an administrator';
      if (this.roles.includes('client')) this.role='You are a client';
      if (this.roles.includes('supplier')) this.role='You are a supplier';
      if (this.roles.includes('repairer')) this.role='You are a repairer';
      this.email = (await this.keycloakService.loadUserProfile()).email;

    }
  }

  confirmDelete() {
    this.userManage.deleteUser(this.id,this.role,this.email).subscribe(
      ()=>{
        this.keycloakService.logout();
      }
    )
  }

}
