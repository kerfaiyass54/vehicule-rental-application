import { Component , ChangeDetectionStrategy} from '@angular/core';
import {KeycloakService} from "../shared/keycloak.service";
import {Router} from "@angular/router";


@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-not-authorized',
    imports: [],
    templateUrl: './not-authorized.component.html',
    styleUrl: './not-authorized.component.css'
})
export class NotAuthorizedComponent {

  roles: string[] = []

  constructor(private keycloak: KeycloakService, private router: Router) {}

  goBack() {

    this.roles = this.keycloak.getRoles();

    if(this.roles.includes('admin')){
      console.log(this.roles.includes('admin'))
      this.router.navigate(['/admin']);
    }
    else if(this.roles.includes('supplier')){
      this.router.navigate(['/supplier']);
    }

    else if(this.roles.includes('client')){
      this.router.navigate(['/client']);
    }

    else if(this.roles.includes('repair')){
      this.router.navigate(['/repair']);
    }
    else{
      this.router.navigate(['/**']);
    }
  }
}
