import {Component, Input, ChangeDetectionStrategy} from '@angular/core';
import {RouterLink} from "@angular/router";
import {KeycloakService} from "../../shared/keycloak.service";


@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-nav-bar',
    imports: [
    RouterLink
],
    templateUrl: './nav-bar.html',
    styleUrl: './nav-bar.css'
})
export class NavBar {

  @Input() mainRoute: any;
  @Input() list: any[] = [];

  constructor(private keycloak:KeycloakService) {

  }


  logout() {
    this.keycloak.logout();
  }
}
