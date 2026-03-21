import { Component , ChangeDetectionStrategy} from '@angular/core';
import {Router} from "@angular/router";
import {KeycloakService} from "../shared/keycloak.service";


@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-error-page',
    imports: [

    ],
    templateUrl: './error-page.component.html',
    styleUrl: './error-page.component.css'
})
export class ErrorPageComponent {

  roles: string[] = []

  constructor(private keycloak: KeycloakService,
              private router: Router) {}

  goBack() {

    this.roles = this.keycloak.getRoles();

    if(this.roles.includes('admin')){
      this.router.navigate(['/admin']);
    } else if(this.roles.includes('supplier')){
      this.router.navigate(['/supplier']);
    } else if(this.roles.includes('client')){
      this.router.navigate(['/client']);
    } else if(this.roles.includes('repair')){
      this.router.navigate(['/repair']);
    } else{
      this.router.navigate(['/**']);
    }
  }

}
