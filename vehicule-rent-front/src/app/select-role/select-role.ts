import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {NavBar} from "../UIs/nav-bar/nav-bar";
import {getKeycloak} from "../shared/keycloak-init";
import {UserManage} from "../user-management/services/user-manage";
import {KeycloakService} from "../shared/keycloak.service";
import {ToastrService} from "ngx-toastr";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatButton} from "@angular/material/button";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-select-role',
  imports: [
    NavBar,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    MatButton
  ],
  templateUrl: './select-role.html',
  styleUrl: './select-role.css',
})
export class SelectRole implements OnInit{

  id: any = '';

  constructor(private userManagement: UserManage,private keycloakService: KeycloakService, private toastrService: ToastrService) {
  }

  async ngOnInit() {
    this.id = (await this.keycloakService.loadUserProfile()).id;
  }


  mainRoute = "/";
  list = [];


  roles = ['client', 'supplier', 'repair','admin'];

  selectRole(role:any){
    this.userManagement.assignRole(this.id, role).subscribe(
      ()=>{
        this.toastrService.success("Role chosen!");
        setTimeout(() => {
          this.keycloakService.logout();
        }, 3000);
      }
    )
  }

}
